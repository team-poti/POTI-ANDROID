package com.poti.android.core.network.interceptor

import com.poti.android.data.local.datasource.PreferenceDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val preferenceDataSource: PreferenceDataSource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()

        if (shouldIgnoreUrl(url)) {
            return chain.proceed(originalRequest)
        }

        val accessToken = runBlocking {
            preferenceDataSource.accessToken.first()
        }

        if (accessToken.isNullOrBlank()) {
            return chain.proceed(originalRequest)
        }

        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(newRequest)
    }

    private fun shouldIgnoreUrl(url: String): Boolean {
        return url.contains("/api/v1/auth/sign-in") ||
            url.contains("/api/v1/auth/sign-up") ||
            url.contains("/api/v1/university/search") ||
            url.contains("/api/v1/major/search")
    }
}
