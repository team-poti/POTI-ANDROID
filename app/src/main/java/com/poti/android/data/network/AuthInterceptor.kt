package com.poti.android.data.network

import com.poti.android.BuildConfig
import com.poti.android.data.local.datasource.AuthTokenStore
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authTokenStore: AuthTokenStore,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()
        Timber.d("요청 URL: $url")

        if (isExcludedAuthPath(originalRequest.url.encodedPath)) {
            return chain.proceed(originalRequest)
        }

        if (!isApiOrigin(originalRequest.url)) {
            return chain.proceed(originalRequest)
        }

        val builder = originalRequest.newBuilder()

        if (!authTokenStore.ensureInitializedBlocking()) {
            Timber.Forest.tag("AuthInterceptor")
                .w("Token store initialization timed out. Proceeding without Authorization header.")
            return chain.proceed(builder.build())
        }

        val accessToken = authTokenStore.cachedAccessToken

        if (!accessToken.isNullOrBlank()) {
            Timber.Forest.tag("AuthInterceptor").d("Adding Authorization Header")
            builder.header("Authorization", "Bearer $accessToken")
        }

        return chain.proceed(builder.build())
    }

    private fun isExcludedAuthPath(path: String): Boolean =
        path == AUTH_LOGIN_PATH || path == AUTH_REISSUE_PATH

    private fun isApiOrigin(requestUrl: HttpUrl): Boolean =
        requestUrl.scheme == apiBaseUrl.scheme &&
            requestUrl.host == apiBaseUrl.host &&
            requestUrl.port == apiBaseUrl.port

    private companion object {
        val apiBaseUrl: HttpUrl = BuildConfig.BASE_URL.toHttpUrl()
        const val AUTH_LOGIN_PATH = "/api/v1/auth/login"
        const val AUTH_REISSUE_PATH = "/api/v1/auth/reissue"
    }
}
