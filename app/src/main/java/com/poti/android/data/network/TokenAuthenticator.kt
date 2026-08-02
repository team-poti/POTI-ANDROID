package com.poti.android.data.network

import com.poti.android.data.local.datasource.AuthTokenStore
import com.poti.android.data.local.datasource.TokenPair
import com.poti.android.data.remote.datasource.AuthRemoteDataSource
import com.poti.android.data.remote.dto.request.auth.ReissueRequestDto
import com.poti.android.domain.manager.AuthSessionManager
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Provider

class TokenAuthenticator @Inject constructor(
    private val authTokenStore: AuthTokenStore,
    private val authRemoteDataSource: Provider<AuthRemoteDataSource>,
    private val authSessionManager: AuthSessionManager,
) : Authenticator {
    private val lock = Any()

    override fun authenticate(
        route: Route?,
        response: Response,
    ): Request? {
        val requestUrl = response.request.url.toString()
        Timber.Forest.tag("TokenAuthenticator").e("401 Unauthorized detected! URL: $requestUrl")

        if (response.authRetryCount() > MAX_AUTH_RETRY_COUNT) {
            Timber.Forest.tag("TokenAuthenticator")
                .w("Stop token reissue because auth retry limit exceeded. URL: $requestUrl")
            return null
        }

        if (isExcludedAuthUrl(requestUrl)) {
            Timber.Forest.tag("TokenAuthenticator")
                .d("Skip token reissue for auth endpoint. URL: $requestUrl")
            return null
        }

        val requestAccessToken = response.request.accessTokenFromAuthorizationHeader()
        if (!authTokenStore.ensureInitializedBlocking()) {
            Timber.Forest.tag("TokenAuthenticator")
                .w("Token store initialization timed out. Stop token reissue.")
            return null
        }

        val refreshResult = synchronized(lock) {
            val latestTokenPair = authTokenStore.cachedTokenPair

            if (latestTokenPair == null) {
                Timber.tag("TokenAuthenticator").w("Token is empty or was cleared. Stop retry.")
                return@synchronized RefreshResult.Stop
            }

            if (requestAccessToken != latestTokenPair.accessToken) {
                Timber.tag("TokenAuthenticator").d("Token already refreshed! Retrying.")
                return@synchronized RefreshResult.Retry(latestTokenPair.accessToken)
            }

            Timber.Forest.tag("TokenAuthenticator").d("Requesting token reissue...")
            val refreshResponse = try {
                authRemoteDataSource.get().reissue(ReissueRequestDto(latestTokenPair.refreshToken)).execute()
            } catch (e: IOException) {
                Timber.Forest.tag("TokenAuthenticator")
                    .w(e, "Reissue API call failed due to network error. Keep session.")
                return@synchronized RefreshResult.Stop
            } catch (e: Exception) {
                Timber.Forest.tag("TokenAuthenticator")
                    .e(e, "Reissue API call failed unexpectedly. Keep session.")
                return@synchronized RefreshResult.Stop
            }

            when (refreshResponse.code()) {
                HTTP_UNAUTHORIZED, HTTP_FORBIDDEN -> {
                    Timber.Forest.tag("TokenAuthenticator")
                        .e("Refresh token is expired or invalid. code=${refreshResponse.code()}. Logout.")
                    val generation = authTokenStore.clearCachedTokens()
                    return@synchronized RefreshResult.Logout(generation)
                }

                in HTTP_SERVER_ERROR_START..HTTP_SERVER_ERROR_END -> {
                    Timber.Forest.tag("TokenAuthenticator")
                        .w("Reissue API returned server error. code=${refreshResponse.code()}. Keep session.")
                    return@synchronized RefreshResult.Stop
                }
            }

            if (!refreshResponse.isSuccessful) {
                Timber.Forest.tag("TokenAuthenticator")
                    .w("Reissue API returned non-success response. code=${refreshResponse.code()}. Keep session.")
                return@synchronized RefreshResult.Stop
            }

            val reissueData = refreshResponse.body()?.data
            if (reissueData == null) {
                Timber.Forest.tag("TokenAuthenticator")
                    .e("Reissue API returned success but body/data is null. Keep session.")
                return@synchronized RefreshResult.Stop
            }

            Timber.Forest.tag("TokenAuthenticator").d("Token reissue SUCCESS! Saving new tokens.")

            val newTokenPair = TokenPair(reissueData.accessToken, reissueData.refreshToken)
            val generation = authTokenStore.updateCachedTokens(newTokenPair)
            RefreshResult.PersistAndRetry(newTokenPair, generation)
        }

        return when (refreshResult) {
            is RefreshResult.Retry -> newRequestWithAccessToken(response.request, refreshResult.accessToken)
            is RefreshResult.PersistAndRetry -> {
                persistTokensBlocking(refreshResult.tokenPair, refreshResult.generation)
                newRequestWithAccessToken(response.request, refreshResult.tokenPair.accessToken)
            }

            is RefreshResult.Logout -> {
                handleLogout(refreshResult.generation)
                null
            }

            RefreshResult.Stop -> null
        }
    }

    private fun newRequestWithAccessToken(
        request: Request,
        newAccessToken: String,
    ): Request {
        Timber.Forest.tag("TokenAuthenticator").d("Rebuilding request with new Bearer token.")

        return request.newBuilder()
            .header(AUTHORIZATION_HEADER, "$BEARER_PREFIX$newAccessToken")
            .build()
    }

    private fun handleLogout(generation: Long) {
        Timber.Forest.tag("TokenAuthenticator").w("Executing Logout logic (Clear DataStore).")
        runCatching {
            runBlocking {
                withTimeout(DATASTORE_PERSIST_TIMEOUT_MILLIS) {
                    authTokenStore.persistTokenClearIfCurrent(generation)
                }
            }
        }.onFailure { error ->
            Timber.Forest.tag("TokenAuthenticator")
                .e(error, "Failed to persist token clear. Continue logout with cleared in-memory tokens.")
        }
        Timber.Forest.tag("TokenAuthenticator").d("Restarting MainActivity to navigate to Login.")
        authSessionManager.triggerLogout()
    }

    private fun persistTokensBlocking(
        tokenPair: TokenPair,
        generation: Long,
    ) {
        runCatching {
            runBlocking {
                withTimeout(DATASTORE_PERSIST_TIMEOUT_MILLIS) {
                    authTokenStore.persistTokensIfCurrent(tokenPair, generation)
                }
            }
        }.onFailure { error ->
            Timber.Forest.tag("TokenAuthenticator")
                .e(error, "Failed to persist refreshed tokens. Keep the updated in-memory token pair.")
        }
    }

    private fun isExcludedAuthUrl(requestUrl: String): Boolean =
        requestUrl.contains(AUTH_LOGIN_PATH) || requestUrl.contains(AUTH_REISSUE_PATH)

    private fun Request.accessTokenFromAuthorizationHeader(): String? =
        header(AUTHORIZATION_HEADER)?.removePrefix(BEARER_PREFIX)?.takeIf { it.isNotBlank() }

    private fun Response.authRetryCount(): Int {
        var count = 1
        var priorResponse = priorResponse

        while (priorResponse != null) {
            count++
            priorResponse = priorResponse.priorResponse
        }

        return count
    }

    private sealed interface RefreshResult {
        data class Retry(val accessToken: String) : RefreshResult

        data class PersistAndRetry(
            val tokenPair: TokenPair,
            val generation: Long,
        ) : RefreshResult

        data class Logout(val generation: Long) : RefreshResult

        data object Stop : RefreshResult
    }

    private companion object {
        const val AUTH_LOGIN_PATH = "/auth/login"
        const val AUTH_REISSUE_PATH = "/auth/reissue"
        const val AUTHORIZATION_HEADER = "Authorization"
        const val BEARER_PREFIX = "Bearer "
        const val MAX_AUTH_RETRY_COUNT = 1
        const val HTTP_UNAUTHORIZED = 401
        const val HTTP_FORBIDDEN = 403
        const val HTTP_SERVER_ERROR_START = 500
        const val HTTP_SERVER_ERROR_END = 599
        const val DATASTORE_PERSIST_TIMEOUT_MILLIS = 1_000L
    }
}
