package com.poti.android.data.remote.dto.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountResponseDto(
    @SerialName("nickname")
    val nickname: String,
    @SerialName("email")
    val email: String,
    @SerialName("socialType")
    val socialType: String,
)
