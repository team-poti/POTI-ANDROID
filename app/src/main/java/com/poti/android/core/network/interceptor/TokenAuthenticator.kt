package com.poti.android.core.network.interceptor

import com.poti.android.core.network.model.AuthType
import com.poti.android.data.local.datasource.PreferenceDataSource
import com.poti.android.data.remote.datasource.AuthRemoteDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class TokenAuthenticator @Inject constructor(
    private val preferenceDataSource: PreferenceDataSource,
    private val authRemoteDataSource: Provider<AuthRemoteDataSource>,
) : Authenticator {
    override fun authenticate(
        route: Route?,
        response: Response,
    ): Request? {
        val requestUrl = response.request.url.toString()
        Timber.tag("TokenAuthenticator").e("401 Unauthorized detected! URL: $requestUrl")

        val currentAccessToken = runBlocking { preferenceDataSource.accessToken.first() }
        val currentRefreshToken = runBlocking { preferenceDataSource.refreshToken.first() }

        if (hasNewToken(response.request, currentAccessToken)) {
            Timber.tag("TokenAuthenticator").d("Token already refreshed by another thread. Retrying with new token.")
            return newRequestWithAccessToken(response.request, currentAccessToken)
        }

        if (currentRefreshToken.isNullOrBlank()) {
            Timber.tag("TokenAuthenticator").e("RefreshToken is empty. Logout.")
            handleLogout()
            return null
        }

        Timber.tag("TokenAuthenticator").d("Requesting token reissue...")
        val refreshResponse = try {
            authRemoteDataSource.get().reissue(currentRefreshToken).execute()
        } catch (e: Exception) {
            Timber.tag("TokenAuthenticator").e(e, "Reissue API call failed (Exception). Logout.")
            handleLogout()
            return null
        }

        if (refreshResponse.isSuccessful) {
            val baseResponse = refreshResponse.body()
            val reissueData = baseResponse?.data

            if (reissueData != null) {
                Timber.tag("TokenAuthenticator").d("Token reissue SUCCESS! Saving new tokens.")

                val newAccessToken = reissueData.accessToken
                val newRefreshToken = reissueData.refreshToken

                runBlocking { preferenceDataSource.saveTokens(newAccessToken, newRefreshToken) }

                return newRequestWithAccessToken(response.request, newAccessToken)
            } else {
                Timber.tag("TokenAuthenticator").e("Reissue success but data is null. Logout.")
            }
        } else {
            Timber.tag("TokenAuthenticator").e("Reissue failed. Code: ${refreshResponse.code()}. Logout.")
        }

        handleLogout()
        return null
    }

    private fun newRequestWithAccessToken(
        request: Request,
        newAccessToken: String?,
    ): Request {
        if (newAccessToken == null) return request

        val authType = request.tag(String::class.java)
        Timber.tag("TokenAuthenticator").d("Rebuilding request with new token. Strategy: $authType")

        val builder = request.newBuilder()

        when (authType) {
            AuthType.ACCESS_TOKEN -> {
                builder.header("Access-Token", newAccessToken)
            }
            AuthType.RAW -> {
                builder.header("Authorization", newAccessToken)
            }
            AuthType.BEARER, null -> {
                builder.header("Authorization", "Bearer $newAccessToken")
            }
        }

        return builder.build()
    }

    private fun hasNewToken(
        request: Request,
        currentToken: String?,
    ): Boolean {
        val authHeader = request.header("Authorization")
        val accessHeader = request.header("Access-Token")

        val isDifferent = (authHeader != null && !authHeader.contains(currentToken ?: "")) ||
            (accessHeader != null && accessHeader != currentToken)

        if (isDifferent) {
            Timber.tag("TokenAuthenticator").d("Detected new token in local storage.")
        }
        return isDifferent
    }

    private fun handleLogout() {
        Timber.tag("TokenAuthenticator").w("Executing Logout logic (Clear DataStore).")
        runBlocking { preferenceDataSource.clear() }
        // TODO: [지현] 로그인 화면으로 이동
    }
}
