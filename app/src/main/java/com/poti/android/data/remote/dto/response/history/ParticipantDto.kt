package com.poti.android.data.remote.dto.response.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticipantDto(
    @SerialName("orderId")
    val orderId: Long,
    @SerialName("userId")
    val userId: Long,
    @SerialName("memberNames")
    val memberNames: List<String>,
    @SerialName("status")
    val status: String,
    @SerialName("priceInfo")
    val priceInfo: PriceInfoDto,
    @SerialName("shippingInfo")
    val shippingInfo: ShippingInfoDto,
)
