package com.poti.android.data.network

import com.poti.android.data.local.datasource.PreferenceDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val preferenceDataSource: PreferenceDataSource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()

        if (url.contains("/auth/login") || url.contains("/auth/reissue")) {
            return chain.proceed(originalRequest)
        }

        val builder = originalRequest.newBuilder()

        val accessToken = runBlocking { preferenceDataSource.accessToken.first() }

        if (!accessToken.isNullOrBlank()) {
            Timber.Forest.tag("AuthInterceptor").d("Adding Authorization Header")
            builder.addHeader("Authorization", "Bearer $accessToken")
        }

        return chain.proceed(builder.build())
    }
}
