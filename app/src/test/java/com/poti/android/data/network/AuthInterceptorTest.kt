package com.poti.android.data.network

import com.poti.android.BuildConfig
import com.poti.android.data.local.datasource.AuthTokenStore
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`

class AuthInterceptorTest {
    private val authTokenStore: AuthTokenStore = mock(AuthTokenStore::class.java)
    private val apiBaseUrl = BuildConfig.BASE_URL.toHttpUrl()

    private lateinit var authInterceptor: AuthInterceptor

    @Before
    fun setUp() {
        authInterceptor = AuthInterceptor(authTokenStore)
        `when`(authTokenStore.ensureInitializedBlocking()).thenReturn(true)
        `when`(authTokenStore.cachedAccessToken).thenReturn(ACCESS_TOKEN)
    }

    @Test
    fun `adds Authorization header only for API origin`() {
        val proceededRequest = intercept(apiUrl("/api/v1/party"))

        assertEquals("Bearer $ACCESS_TOKEN", proceededRequest.header(AUTHORIZATION_HEADER))
        verify(authTokenStore).ensureInitializedBlocking()
    }

    @Test
    fun `does not add Authorization header for external origin`() {
        val proceededRequest = intercept("https://external.example.com/api/v1/party")

        assertNull(proceededRequest.header(AUTHORIZATION_HEADER))
        verifyNoInteractions(authTokenStore)
    }

    @Test
    fun `does not add Authorization header for excluded authentication endpoints`() {
        val loginRequest = intercept(apiUrl("/api/v1/auth/login"))
        val reissueRequest = intercept(apiUrl("/api/v1/auth/reissue"))

        assertNull(loginRequest.header(AUTHORIZATION_HEADER))
        assertNull(reissueRequest.header(AUTHORIZATION_HEADER))
        verifyNoInteractions(authTokenStore)
    }

    private fun intercept(requestUrl: String): Request {
        val originalRequest = Request.Builder().url(requestUrl).build()
        val chain: Interceptor.Chain = mock(Interceptor.Chain::class.java)
        `when`(chain.request()).thenReturn(originalRequest)
        `when`(chain.proceed(any(Request::class.java) ?: originalRequest)).thenReturn(mock(Response::class.java))

        authInterceptor.intercept(chain)

        val requestCaptor = ArgumentCaptor.forClass(Request::class.java)
        verify(chain).proceed(requestCaptor.capture() ?: originalRequest)
        return requestCaptor.value
    }

    private fun apiUrl(path: String): String = apiBaseUrl.newBuilder()
        .encodedPath(path)
        .build()
        .toString()

    private companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val ACCESS_TOKEN = "access-token"
    }
}
