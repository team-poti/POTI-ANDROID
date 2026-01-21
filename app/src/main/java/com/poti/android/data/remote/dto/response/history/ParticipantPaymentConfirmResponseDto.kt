package com.poti.android.data.remote.dto.response.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticipantPaymentConfirmResponseDto(
    @SerialName("orderId")
    val orderId: Long,
    @SerialName("status")
    val status: String,
    @SerialName("confirmedAt")
    val confirmedAt: String,
)
