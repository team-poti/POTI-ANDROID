package com.poti.android.data.remote.dto.request.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WithdrawalRequestDto(
    @SerialName("reason")
    val reason: String,
)
