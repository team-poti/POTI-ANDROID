package com.poti.android.domain.model

data class UserAuth(
    val accessToken: String,
    val refreshToken: String,
    val isNewUser: Boolean,
    val userId: Long,
)
