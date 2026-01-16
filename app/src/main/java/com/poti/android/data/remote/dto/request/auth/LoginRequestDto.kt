package com.poti.android.data.remote.dto.request.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    @SerialName("socialType")
    val socialType: String,
    @SerialName("token")
    val token: String,
)
