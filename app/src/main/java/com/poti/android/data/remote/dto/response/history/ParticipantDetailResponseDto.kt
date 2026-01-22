package com.poti.android.data.remote.dto.response.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticipantDetailResponseDto(
    @SerialName("participationId")
    val participationId: Long,
    @SerialName("postId")
    val postId: Long,
    @SerialName("orderNumber")
    val orderNumber: String,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("artistName")
    val artistName: String,
    @SerialName("title")
    val title: String,
    @SerialName("status")
    val status: String,
    @SerialName("statusMessage")
    val statusMessage: String,
    @SerialName("memberPayments")
    val memberPayments: List<MemberPaymentDto>,
    @SerialName("paymentInfo")
    val paymentInfo: PaymentInfoDto,
    @SerialName("shippingInfo")
    val shippingInfo: ShippingInfoDto,
) {
    @Serializable
    data class MemberPaymentDto(
        @SerialName("memberName")
        val memberName: String,
        @SerialName("price")
        val price: Int,
    )

    @Serializable
    data class PaymentInfoDto(
        @SerialName("shippingFee")
        val shippingFee: Int,
        @SerialName("totalAmount")
        val totalAmount: Int,
        @SerialName("depositStatus")
        val depositStatus: String,
        @SerialName("bank")
        val bank: String?,
        @SerialName("accountNumber")
        val accountNumber: String?,
        @SerialName("depositDeadline")
        val depositDeadline: String?,
    )

    @Serializable
    data class ShippingInfoDto(
        @SerialName("shippingMethod")
        val shippingMethod: String,
        @SerialName("receiver")
        val receiver: String,
        @SerialName("zipcode")
        val zipcode: String,
        @SerialName("address")
        val address: String,
        @SerialName("phone")
        val phone: String,
        @SerialName("carrier")
        val carrier: String?,
        @SerialName("trackingNumber")
        val trackingNumber: String?,
        @SerialName("shippingStatus")
        val shippingStatus: String,
    )
}
