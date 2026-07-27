package com.poti.android.core.auth

sealed interface SocialLoginResult {
    data class Success(val token: String) : SocialLoginResult

    data object Cancelled : SocialLoginResult

    data class Failure(val cause: Throwable) : SocialLoginResult
}
