package com.poti.android.data.remote.dto.response.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentResponseDto(
    @SerialName("paymentId")
    val paymentId: Long,
)
