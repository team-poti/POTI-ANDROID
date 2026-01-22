package com.poti.android.data.remote.dto.response.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupBuyPostParticipantDetailDto(
    @SerialName("participants")
    val participants: List<ParticipantDetailDto>,
)

@Serializable
data class ParticipantDetailDto(
    @SerialName("orderId")
    val orderId: Long,
    @SerialName("userId")
    val userId: Long,
    @SerialName("profileImage")
    val profileImage: String,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("memberNames")
    val memberNames: List<String>,
    @SerialName("status")
    val status: String,
    @SerialName("priceInfo")
    val priceInfo: ParticipantPriceInfoDto,
    @SerialName("depositInfo")
    val depositInfo: ParticipantDepositInfoDto?,
    @SerialName("shippingInfo")
    val shippingInfo: ParticipantShippingInfoDto?,
)

@Serializable
data class ParticipantPriceInfoDto(
    @SerialName("memberPerPrices")
    val memberPerPrices: List<MemberPerPriceDto>,
    @SerialName("shippingName")
    val shippingName: String,
    @SerialName("shippingPrice")
    val shippingPrice: Int,
    @SerialName("totalPrice")
    val totalPrice: Int,
)

@Serializable
data class MemberPerPriceDto(
    @SerialName("name")
    val name: String,
    @SerialName("price")
    val price: Int,
)

@Serializable
data class ParticipantDepositInfoDto(
    @SerialName("depositorName")
    val depositorName: String,
    @SerialName("depositTime")
    val depositTime: String,
)

@Serializable
data class ParticipantShippingInfoDto(
    @SerialName("receiverName")
    val receiverName: String?,
    @SerialName("address")
    val address: String?,
    @SerialName("phone")
    val phone: String?,
    @SerialName("trackingNumber")
    val trackingNumber: String?,
)
