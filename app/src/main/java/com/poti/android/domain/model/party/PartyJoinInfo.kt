package com.poti.android.domain.model.party

data class PartyJoinInfo(
    val partyId: Long,
    val shippingOptionId: Long,
    val deliveryInfo: DeliveryInfo,
    val joinItems: List<JoinOption>,
)

data class DeliveryInfo(
    val receiverName: String,
    val zipcode: String,
    val address: String,
    val phoneNumber: String,
)

data class JoinOption(
    val optionId: Long,
    val count: Int,
)
