package com.poti.android.data.network

import com.poti.android.BuildConfig
import com.poti.android.data.local.datasource.AuthTokenStore
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

        if (url.contains("/auth/login") || url.contains("/auth/reissue")) {
            return chain.proceed(originalRequest)
        }

        if (!url.contains(BuildConfig.BASE_URL)) {
            return chain.proceed(originalRequest)
        }

        val builder = originalRequest.newBuilder()

        authTokenStore.ensureInitializedBlocking()
        val accessToken = authTokenStore.cachedAccessToken

        if (!accessToken.isNullOrBlank()) {
            Timber.Forest.tag("AuthInterceptor").d("Adding Authorization Header")
            builder.addHeader("Authorization", "Bearer $accessToken")
        }

        return chain.proceed(builder.build())
    }
}
