package com.poti.android.data.remote.dto.response.post

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShippingOptionResponseDto(
    @SerialName("deliveryId")
    val deliveryId: Long,
    @SerialName("name")
    val name: String,
    @SerialName("price")
    val price: Int,
)
