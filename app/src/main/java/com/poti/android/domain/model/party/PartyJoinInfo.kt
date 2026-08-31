package com.poti.android.domain.model.party

import com.poti.android.domain.model.delivery.DeliveryInfo

data class PartyJoinInfo(
    val partyId: Long,
    val shippingOptionId: Long,
    val deliveryInfo: DeliveryInfo,
    val joinItems: List<JoinOption>,
)

data class JoinOption(
    val optionId: Long,
    val count: Int,
)
