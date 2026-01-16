package com.poti.android.domain.model.auth

data class UserAuth(
    val accessToken: String,
    val refreshToken: String,
    val isNewUser: Boolean,
    val userId: Long,
)
