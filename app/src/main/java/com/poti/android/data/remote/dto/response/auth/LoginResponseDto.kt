package com.poti.android.data.remote.dto.response.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("isNewUser")
    val isNewUser: Boolean,
    @SerialName("userId")
    val userId: Long,
)
