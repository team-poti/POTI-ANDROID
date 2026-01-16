package com.poti.android.core.network.interceptor

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
        val authType = originalRequest.header("Auth-Type")
        val url = originalRequest.url.toString()

        Timber.tag("AuthInterceptor").d("Intercepting: $url | AuthType: ${authType ?: "None"}")

        if (authType == null || authType == AuthType.NO_AUTH) {
            val newRequest = originalRequest.newBuilder()
                .removeHeader("Auth-Type")
                .build()
            return chain.proceed(newRequest)
        }

        val accessToken = runBlocking {
            preferenceDataSource.accessToken.first()
        }

        if (accessToken.isNullOrBlank()) {
            Timber.tag("AuthInterceptor").w("AccessToken is empty. Proceeding without header.")
            val newRequest = originalRequest.newBuilder()
                .removeHeader("Auth-Type")
                .build()
            return chain.proceed(newRequest)
        }

        val builder = originalRequest.newBuilder()
        builder.tag(String::class.java, authType)
        builder.removeHeader("Auth-Type")

        when (authType) {
            AuthType.BEARER -> {
                Timber.tag("AuthInterceptor").d("Injecting Bearer Token")
                builder.addHeader("Authorization", "Bearer $accessToken")
            }
            AuthType.RAW -> {
                Timber.tag("AuthInterceptor").d("Injecting Raw Token")
                builder.addHeader("Authorization", accessToken)
            }
            AuthType.ACCESS_TOKEN -> {
                Timber.tag("AuthInterceptor").d("Injecting Access-Token Header")
                builder.addHeader("Access-Token", accessToken)
            }
            else -> {
                builder.addHeader("Authorization", "Bearer $accessToken")
            }
        }

        return chain.proceed(builder.build())
    }
}
