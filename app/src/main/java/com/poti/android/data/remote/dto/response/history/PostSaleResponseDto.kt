package com.poti.android.data.remote.dto.response.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostSaleResponseDto(
    @SerialName("postId")
    val postId: Long,
    @SerialName("orderNumber")
    val orderNumber: String,
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("artistName")
    val artistName: String,
    @SerialName("title")
    val title: String,
    @SerialName("postStatus")
    val postStatus: String,
    @SerialName("statusMessage")
    val statusMessage: String,
    @SerialName("participant")
    val participant: List<ParticipantDto>,
)

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

@Serializable
data class PriceInfoDto(
    @SerialName("shippingName")
    val shippingName: String,
    @SerialName("totalPrice")
    val totalPrice: Int,
)

@Serializable
data class ShippingInfoDto(
    @SerialName("receiverName")
    val receiverName: String,
    @SerialName("address")
    val address: String,
    @SerialName("phone")
    val phone: String,
)
