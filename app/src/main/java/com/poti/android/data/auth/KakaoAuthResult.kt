package com.poti.android.data.auth

sealed interface KakaoAuthResult {
    data class Success(val accessToken: String) : KakaoAuthResult

    data object Cancelled : KakaoAuthResult

    data class Failure(val cause: Throwable) : KakaoAuthResult

    data class InvalidResponse(val cause: Throwable) : KakaoAuthResult
}
