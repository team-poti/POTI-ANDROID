package com.poti.android.data.remote.dto.response.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShippingInfoDto(
    @SerialName("receiverName")
    val receiverName: String,
    @SerialName("address")
    val address: String,
    @SerialName("phone")
    val phone: String,
)
