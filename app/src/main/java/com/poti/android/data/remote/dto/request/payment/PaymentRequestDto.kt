package com.poti.android.data.remote.dto.request.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentRequestDto(
    @SerialName("orderId")
    val orderId: Long,
    @SerialName("depositorName")
    val depositorName: String,
    @SerialName("depositedAt")
    val depositedAt: String
)
