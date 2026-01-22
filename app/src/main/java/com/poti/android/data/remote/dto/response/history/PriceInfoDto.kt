package com.poti.android.data.remote.dto.response.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriceInfoDto(
    @SerialName("shippingName")
    val shippingName: String,
    @SerialName("totalPrice")
    val totalPrice: Int,
)
