package com.poti.android.domain.model.auth

data class AuthState(
    val accessToken: String?,
    val isOnboardingFinished: Boolean,
    val isInitialized: Boolean = false,
)
