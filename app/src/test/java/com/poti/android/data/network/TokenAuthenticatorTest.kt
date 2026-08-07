package com.poti.android.data.network

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.local.datasource.AuthTokenStore
import com.poti.android.data.local.datasource.TokenPair
import com.poti.android.data.remote.datasource.AuthRemoteDataSource
import com.poti.android.data.remote.dto.request.auth.ReissueRequestDto
import com.poti.android.data.remote.dto.response.auth.ReissueResponseDto
import com.poti.android.domain.manager.AuthSessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import retrofit2.Call
import java.io.IOException
import javax.inject.Provider
import retrofit2.Response as RetrofitResponse

class TokenAuthenticatorTest {
    private val authTokenStore: AuthTokenStore = mock(AuthTokenStore::class.java)
    private val authRemoteDataSource: AuthRemoteDataSource = mock(AuthRemoteDataSource::class.java)
    private val authSessionManager: AuthSessionManager = mock(AuthSessionManager::class.java)

    private lateinit var tokenAuthenticator: TokenAuthenticator

    @Before
    fun setUp() {
        tokenAuthenticator = TokenAuthenticator(
            authTokenStore = authTokenStore,
            authRemoteDataSource = Provider { authRemoteDataSource },
            authSessionManager = authSessionManager,
        )

        `when`(authTokenStore.ensureInitializedBlocking()).thenReturn(true)
        `when`(authTokenStore.cachedTokenPair).thenReturn(OLD_TOKEN_PAIR)
        `when`(authTokenStore.updateCachedTokens(NEW_TOKEN_PAIR)).thenReturn(TOKEN_GENERATION)
    }

    @Test
    fun `reissues token and retries request with new access token when original request returns 401`() {
        val reissueCall = reissueCallReturningNewTokens()
        `when`(authRemoteDataSource.reissue(ReissueRequestDto(OLD_REFRESH_TOKEN)))
            .thenReturn(reissueCall)

        val retriedRequest = tokenAuthenticator.authenticate(
            route = null,
            response = unauthorizedResponse(),
        )

        assertNotNull(retriedRequest)
        assertEquals(
            "Bearer $NEW_ACCESS_TOKEN",
            retriedRequest?.header(AUTHORIZATION_HEADER),
        )
        verify(authRemoteDataSource).reissue(ReissueRequestDto(OLD_REFRESH_TOKEN))
        verify(authTokenStore).updateCachedTokens(NEW_TOKEN_PAIR)
        runBlocking {
            verify(authTokenStore).persistTokensIfCurrent(NEW_TOKEN_PAIR, TOKEN_GENERATION)
        }
    }

    @Test
    fun `logs out when reissue returns 400`() {
        assertInvalidRefreshTokenLogsOut(HTTP_BAD_REQUEST)
    }

    @Test
    fun `logs out when reissue returns 404`() {
        assertInvalidRefreshTokenLogsOut(HTTP_NOT_FOUND)
    }

    @Test
    fun `logs out when reissue returns 401`() {
        assertInvalidRefreshTokenLogsOut(HTTP_UNAUTHORIZED)
    }

    @Test
    fun `logs out when reissue returns 403`() {
        assertInvalidRefreshTokenLogsOut(HTTP_FORBIDDEN)
    }

    @Test
    fun `keeps session when reissue returns server error`() {
        stubReissueResponse(RetrofitResponse.error(HTTP_INTERNAL_SERVER_ERROR, "Server error".toResponseBody()))

        val retriedRequest = tokenAuthenticator.authenticate(null, unauthorizedResponse())

        assertNull(retriedRequest)
        verify(authTokenStore, never()).clearCachedTokens()
        verify(authSessionManager, never()).triggerLogout()
    }

    @Test
    fun `keeps session when reissue throws IOException`() {
        val reissueCall = mockReissueCall()
        `when`(reissueCall.execute()).thenThrow(IOException("Network unavailable"))
        `when`(authRemoteDataSource.reissue(ReissueRequestDto(OLD_REFRESH_TOKEN)))
            .thenReturn(reissueCall)

        val retriedRequest = tokenAuthenticator.authenticate(null, unauthorizedResponse())

        assertNull(retriedRequest)
        verify(authTokenStore, never()).clearCachedTokens()
        verify(authSessionManager, never()).triggerLogout()
    }

    @Test
    fun `keeps session when reissue returns success with null body data`() {
        stubReissueResponse(
            RetrofitResponse.success(
                BaseResponse(
                    code = 200,
                    message = "Success",
                    data = null,
                ),
            ),
        )

        val retriedRequest = tokenAuthenticator.authenticate(null, unauthorizedResponse())

        assertNull(retriedRequest)
        verify(authTokenStore, never()).clearCachedTokens()
        verify(authSessionManager, never()).triggerLogout()
    }

    @Test
    fun `does not reissue when response already has an authentication retry`() {
        val retriedRequest = tokenAuthenticator.authenticate(
            route = null,
            response = unauthorizedResponse(previousResponse = unauthorizedResponse()),
        )

        assertNull(retriedRequest)
        verifyNoInteractions(authRemoteDataSource)
    }

    @Test
    fun `does not reissue login endpoint`() {
        val retriedRequest = tokenAuthenticator.authenticate(
            route = null,
            response = unauthorizedResponse(requestUrl = "$API_ORIGIN/api/v1/auth/login?source=test"),
        )

        assertNull(retriedRequest)
        verifyNoInteractions(authRemoteDataSource)
    }

    @Test
    fun `does not reissue reissue endpoint`() {
        val retriedRequest = tokenAuthenticator.authenticate(
            route = null,
            response = unauthorizedResponse(requestUrl = "$API_ORIGIN/api/v1/auth/reissue"),
        )

        assertNull(retriedRequest)
        verifyNoInteractions(authRemoteDataSource)
    }

    @Suppress("UNCHECKED_CAST")
    private fun reissueCallReturningNewTokens(): Call<BaseResponse<ReissueResponseDto>> {
        val reissueCall = mock(Call::class.java) as Call<BaseResponse<ReissueResponseDto>>
        `when`(reissueCall.execute()).thenReturn(
            RetrofitResponse.success(
                BaseResponse(
                    code = 200,
                    message = "Success",
                    data = ReissueResponseDto(
                        accessToken = NEW_ACCESS_TOKEN,
                        refreshToken = NEW_REFRESH_TOKEN,
                    ),
                ),
            ),
        )
        return reissueCall
    }

    @Test
    fun `does not trigger logout when a newer auth generation is active`() {
        `when`(authTokenStore.clearCachedTokens()).thenReturn(TOKEN_GENERATION)
        runBlocking {
            `when`(authTokenStore.persistTokenClearIfCurrent(TOKEN_GENERATION)).thenReturn(true)
        }
        `when`(authTokenStore.isLogoutGenerationCurrent(TOKEN_GENERATION)).thenReturn(false)
        stubReissueResponse(
            RetrofitResponse.error(HTTP_UNAUTHORIZED, "Invalid refresh token".toResponseBody()),
        )

        val retriedRequest = tokenAuthenticator.authenticate(null, unauthorizedResponse())

        assertNull(retriedRequest)
        verify(authSessionManager, never()).triggerLogout()
    }

    private fun assertInvalidRefreshTokenLogsOut(responseCode: Int) {
        `when`(authTokenStore.clearCachedTokens()).thenReturn(TOKEN_GENERATION)
        runBlocking {
            `when`(authTokenStore.persistTokenClearIfCurrent(TOKEN_GENERATION)).thenReturn(true)
        }
        `when`(authTokenStore.isLogoutGenerationCurrent(TOKEN_GENERATION)).thenReturn(true)
        stubReissueResponse(RetrofitResponse.error(responseCode, "Invalid refresh token".toResponseBody()))

        val retriedRequest = tokenAuthenticator.authenticate(null, unauthorizedResponse())

        assertNull(retriedRequest)
        verify(authTokenStore).clearCachedTokens()
        runBlocking {
            verify(authTokenStore).persistTokenClearIfCurrent(TOKEN_GENERATION)
        }
        verify(authSessionManager).triggerLogout()
    }

    private fun stubReissueResponse(response: RetrofitResponse<BaseResponse<ReissueResponseDto>>) {
        val reissueCall = mockReissueCall()
        `when`(reissueCall.execute()).thenReturn(response)
        `when`(authRemoteDataSource.reissue(ReissueRequestDto(OLD_REFRESH_TOKEN)))
            .thenReturn(reissueCall)
    }

    @Suppress("UNCHECKED_CAST")
    private fun mockReissueCall(): Call<BaseResponse<ReissueResponseDto>> =
        mock(Call::class.java) as Call<BaseResponse<ReissueResponseDto>>

    private fun unauthorizedResponse(
        requestUrl: String = PARTY_URL,
        previousResponse: Response? = null,
    ): Response = Response.Builder()
        .request(
            Request.Builder()
                .url(requestUrl)
                .header(AUTHORIZATION_HEADER, "Bearer $OLD_ACCESS_TOKEN")
                .build(),
        )
        .protocol(Protocol.HTTP_1_1)
        .code(401)
        .message("Unauthorized")
        .apply {
            previousResponse?.let { priorResponse(it) }
        }
        .build()

    private companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val API_ORIGIN = "https://api.example.com"
        const val PARTY_URL = "$API_ORIGIN/api/v1/party"
        const val OLD_ACCESS_TOKEN = "old-access-token"
        const val OLD_REFRESH_TOKEN = "old-refresh-token"
        const val NEW_ACCESS_TOKEN = "new-access-token"
        const val NEW_REFRESH_TOKEN = "new-refresh-token"
        const val TOKEN_GENERATION = 1L
        const val HTTP_BAD_REQUEST = 400
        const val HTTP_NOT_FOUND = 404
        const val HTTP_UNAUTHORIZED = 401
        const val HTTP_FORBIDDEN = 403
        const val HTTP_INTERNAL_SERVER_ERROR = 500

        val OLD_TOKEN_PAIR = TokenPair(
            accessToken = OLD_ACCESS_TOKEN,
            refreshToken = OLD_REFRESH_TOKEN,
        )
        val NEW_TOKEN_PAIR = TokenPair(
            accessToken = NEW_ACCESS_TOKEN,
            refreshToken = NEW_REFRESH_TOKEN,
        )
    }
}
