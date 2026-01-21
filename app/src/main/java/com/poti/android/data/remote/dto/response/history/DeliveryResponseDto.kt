package com.poti.android.data.remote.dto.response.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeliveryResponseDto(
    @SerialName("orderId")
    val orderId: Long,
    @SerialName("status")
    val status: String,
    @SerialName("trackingNumber")
    val trackingNumber: String,
    @SerialName("shippedAt")
    val shippedAt: String
)
