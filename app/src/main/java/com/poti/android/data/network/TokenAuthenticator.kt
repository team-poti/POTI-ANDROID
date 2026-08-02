package com.poti.android.data.network

import com.poti.android.data.local.datasource.PreferenceDataSource
import com.poti.android.data.remote.datasource.AuthRemoteDataSource
import com.poti.android.data.remote.dto.request.auth.ReissueRequestDto
import com.poti.android.domain.manager.AuthSessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Provider

class TokenAuthenticator @Inject constructor(
    private val preferenceDataSource: PreferenceDataSource,
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

        if (isExcludedAuthUrl(requestUrl)) {
            Timber.Forest.tag("TokenAuthenticator")
                .d("Skip token reissue for auth endpoint. URL: $requestUrl")
            return null
        }

        val currentAccessToken = preferenceDataSource.cachedAccessToken
        val currentRefreshToken = preferenceDataSource.cachedRefreshToken

        synchronized(lock) {
            val freshAccessToken = preferenceDataSource.cachedAccessToken

            if (freshAccessToken != currentAccessToken) {
                Timber.tag("TokenAuthenticator").d("Token already refreshed! Retrying.")
                return newRequestWithAccessToken(response.request, freshAccessToken)
            }

            if (currentRefreshToken.isNullOrBlank()) {
                Timber.Forest.tag("TokenAuthenticator").e("RefreshToken is empty. Logout.")
                handleLogout()
                return null
            }

            Timber.Forest.tag("TokenAuthenticator").d("Requesting token reissue...")
            val refreshResponse = try {
                authRemoteDataSource.get().reissue(ReissueRequestDto(currentRefreshToken)).execute()
            } catch (e: IOException) {
                Timber.Forest.tag("TokenAuthenticator")
                    .w(e, "Reissue API call failed due to network error. Keep session.")
                return null
            } catch (e: Exception) {
                Timber.Forest.tag("TokenAuthenticator")
                    .e(e, "Reissue API call failed unexpectedly. Keep session.")
                return null
            }

            when (refreshResponse.code()) {
                HTTP_UNAUTHORIZED, HTTP_FORBIDDEN -> {
                    Timber.Forest.tag("TokenAuthenticator")
                        .e("Refresh token is expired or invalid. code=${refreshResponse.code()}. Logout.")
                    handleLogout()
                    return null
                }

                in HTTP_SERVER_ERROR_START..HTTP_SERVER_ERROR_END -> {
                    Timber.Forest.tag("TokenAuthenticator")
                        .w("Reissue API returned server error. code=${refreshResponse.code()}. Keep session.")
                    return null
                }
            }

            if (!refreshResponse.isSuccessful) {
                Timber.Forest.tag("TokenAuthenticator")
                    .w("Reissue API returned non-success response. code=${refreshResponse.code()}. Keep session.")
                return null
            }

            val reissueData = refreshResponse.body()?.data
            if (reissueData == null) {
                Timber.Forest.tag("TokenAuthenticator")
                    .e("Reissue API returned success but body/data is null. Keep session.")
                return null
            }

            Timber.Forest.tag("TokenAuthenticator").d("Token reissue SUCCESS! Saving new tokens.")

            val newAccessToken = reissueData.accessToken
            val newRefreshToken = reissueData.refreshToken

            runBlocking {
                preferenceDataSource.saveTokens(newAccessToken, newRefreshToken)
            }

            return newRequestWithAccessToken(response.request, newAccessToken)
        }

        Timber.Forest.tag("TokenAuthenticator").w("Token reissue was not completed. Keep session.")
        return null
    }

    private fun newRequestWithAccessToken(
        request: Request,
        newAccessToken: String?,
    ): Request {
        if (newAccessToken == null) return request

        Timber.Forest.tag("TokenAuthenticator").d("Rebuilding request with new Bearer token.")

        return request.newBuilder()
            .header("Authorization", "Bearer $newAccessToken")
            .build()
    }

    private fun handleLogout() {
        Timber.Forest.tag("TokenAuthenticator").w("Executing Logout logic (Clear DataStore).")
        runBlocking {
            preferenceDataSource.clearTokens()
            Timber.Forest.tag("TokenAuthenticator").d("Restarting MainActivity to navigate to Login.")
            authSessionManager.triggerLogout()
        }
    }

    private fun isExcludedAuthUrl(requestUrl: String): Boolean =
        requestUrl.contains(AUTH_LOGIN_PATH) || requestUrl.contains(AUTH_REISSUE_PATH)

    private companion object {
        const val AUTH_LOGIN_PATH = "/auth/login"
        const val AUTH_REISSUE_PATH = "/auth/reissue"
        const val HTTP_UNAUTHORIZED = 401
        const val HTTP_FORBIDDEN = 403
        const val HTTP_SERVER_ERROR_START = 500
        const val HTTP_SERVER_ERROR_END = 599
    }
}
