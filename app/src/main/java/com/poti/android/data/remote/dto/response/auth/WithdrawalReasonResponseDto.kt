package com.poti.android.data.remote.dto.response.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WithdrawalReasonResponseDto(
    @SerialName("code")
    val code: String,
    @SerialName("label")
    val label: String,
)
