package com.poti.android.domain.model.party

import com.poti.android.domain.model.delivery.DeliveryOption

data class PartyJoinOption(
    val memberOptions: List<Members>,
    val deliveryOptions: List<DeliveryOption>,
)

data class Members(
    val memberId: Long,
    val memberName: String,
    val memberPrice: Int,
)
