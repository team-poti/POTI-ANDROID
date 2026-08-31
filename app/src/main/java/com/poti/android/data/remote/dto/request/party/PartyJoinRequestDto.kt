package com.poti.android.data.remote.dto.request.party

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PartyJoinRequestDto(
    @SerialName("groupBuyPostId")
    val groupBuyPostId: Long,
    @SerialName("shippingId")
    val shippingId: Long,
    @SerialName("deliveryInfo")
    val deliveryInfoDto: DeliveryInfoDto,
    @SerialName("items")
    val items: List<JoinOptionDto>,
)

@Serializable
data class DeliveryInfoDto(
    @SerialName("receiverName")
    val receiverName: String,
    @SerialName("zipcode")
    val zipcode: String,
    @SerialName("address")
    val address: String,
    @SerialName("addressDetail")
    val addressDetail: String,
    @SerialName("phone")
    val phone: String,
)

@Serializable
data class JoinOptionDto(
    @SerialName("groupBuyOptionId")
    val groupBuyOptionId: Long,
    @SerialName("count")
    val count: Int,
)
