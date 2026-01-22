package com.poti.android.data.remote.dto.request.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeliveryRequestDto(
    @SerialName("carrier")
    val carrier: String,
    @SerialName("trackingNumber")
    val trackingNumber: String,
)
