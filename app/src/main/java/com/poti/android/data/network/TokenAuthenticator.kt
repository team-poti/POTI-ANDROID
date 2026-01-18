package com.poti.android.data.network

import android.content.Context
import android.content.Intent
import com.poti.android.data.local.datasource.PreferenceDataSource
import com.poti.android.data.remote.datasource.AuthRemoteDataSource
import com.poti.android.data.remote.dto.request.auth.ReissueRequestDto
import com.poti.android.presentation.main.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @ApplicationContext private val context: Context,
) : Authenticator {
    override fun authenticate(
        route: Route?,
        response: Response,
    ): Request? {
        val requestUrl = response.request.url.toString()
        Timber.Forest.tag("TokenAuthenticator").e("401 Unauthorized detected! URL: $requestUrl")

        val currentAccessToken = runBlocking { preferenceDataSource.accessToken.first() }
        val currentRefreshToken = runBlocking { preferenceDataSource.refreshToken.first() }

        if (hasNewToken(response.request, currentAccessToken)) {
            Timber.Forest.tag("TokenAuthenticator").d("Token already refreshed by another thread. Retrying with new token.")
            return newRequestWithAccessToken(response.request, currentAccessToken)
        }

        if (currentRefreshToken.isNullOrBlank()) {
            Timber.Forest.tag("TokenAuthenticator").e("RefreshToken is empty. Logout.")
            handleLogout()
            return null
        }

        Timber.Forest.tag("TokenAuthenticator").d("Requesting token reissue...")
        val refreshResponse = try {
            authRemoteDataSource.get().reissue(ReissueRequestDto(currentRefreshToken)).execute()
        } catch (e: Exception) {
            Timber.Forest.tag("TokenAuthenticator").e(e, "Reissue API call failed (Exception). Logout.")
            handleLogout()
            return null
        }

        if (refreshResponse.isSuccessful) {
            val baseResponse = refreshResponse.body()
            val reissueData = baseResponse?.data

            if (reissueData != null) {
                Timber.Forest.tag("TokenAuthenticator").d("Token reissue SUCCESS! Saving new tokens.")

                val newAccessToken = reissueData.accessToken
                val newRefreshToken = reissueData.refreshToken

                runBlocking { preferenceDataSource.saveTokens(newAccessToken, newRefreshToken) }

                return newRequestWithAccessToken(response.request, newAccessToken)
            } else {
                Timber.Forest.tag("TokenAuthenticator").e("Reissue success but data is null. Logout.")
            }
        } else {
            Timber.Forest.tag("TokenAuthenticator").e("Reissue failed. Code: ${refreshResponse.code()}. Logout.")
        }

        handleLogout()
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

    private fun hasNewToken(
        request: Request,
        currentToken: String?,
    ): Boolean {
        val authHeader = request.header("Authorization")

        val isDifferent = (authHeader != null && !authHeader.contains(currentToken ?: ""))

        if (isDifferent) {
            Timber.Forest.tag("TokenAuthenticator").d("Detected new token in local storage.")
        }
        return isDifferent
    }

    private fun handleLogout() {
        Timber.Forest.tag("TokenAuthenticator").w("Executing Logout logic (Clear DataStore).")
        runBlocking { preferenceDataSource.clearTokens() }

        Timber.Forest.tag("TokenAuthenticator").d("Restarting MainActivity to navigate to Login.")

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }
}
