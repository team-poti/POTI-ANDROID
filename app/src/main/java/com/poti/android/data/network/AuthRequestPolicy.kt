package com.poti.android.data.network

internal object AuthRequestPolicy {
    const val LOGIN_PATH = "/api/v1/auth/login"
    const val REISSUE_PATH = "/api/v1/auth/reissue"
    const val AUTHORIZATION_HEADER = "Authorization"
    const val BEARER_PREFIX = "Bearer "

    fun isExcludedAuthPath(path: String): Boolean =
        path == LOGIN_PATH || path == REISSUE_PATH
}
