package com.poti.android.domain.model.history

import com.poti.android.domain.type.ParticipantStatusType

data class ParticipantManageDetail(
    val participants: List<ParticipantDetailInfo>,
)

data class ParticipantDetailInfo(
    val orderId: Long,
    val userId: Long,
    val profileImage: String?,
    val nickname: String,
    val participantStatus: ParticipantStatusType,
    val memberNames: List<String>,
    val priceInfo: List<MemberPriceInfo>,
    val shippingName: String,
    val shippingPrice: Int,
    val totalPrice: Int,
    val depositInfo: DepositInfo?,
    val shippingInfo: ShippingInfo?,
)

data class DepositInfo(
    val depositorName: String,
    val depositTime: String,
)

data class MemberPriceInfo(
    val name: String,
    val price: Int,
)
