package com.poti.android.data.remote.dto.response.party

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PartyJoinOptionsDto(
    @SerialName("members")
    val members: List<MemberOptionDto>,
    @SerialName("shippings")
    val shippings: List<DeliveryOptionDto>,
)

@Serializable
data class MemberOptionDto(
    @SerialName("memberOptionId")
    val memberOptionId: Long,
    @SerialName("memberName")
    val memberName: String,
    @SerialName("memberOptionPrice")
    val memberOptionPrice: Int,
)

@Serializable
data class DeliveryOptionDto(
    @SerialName("deliveryOptionId")
    val deliveryOptionId: Long,
    @SerialName("deliveryName")
    val deliveryName: String,
    @SerialName("deliveryOptionPrice")
    val deliveryOptionPrice: Int,
)
