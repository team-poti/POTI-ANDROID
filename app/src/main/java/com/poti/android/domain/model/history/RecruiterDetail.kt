package com.poti.android.domain.model.history

import com.poti.android.domain.type.ParticipantStatusType

data class RecruiterDetail(
    val recruitId: Long,
    val orderNumber: String,
    val partySummary: PartySummary,
    val participantInfoList: List<ParticipantInfo>,
    val participantCount: Int,
)

data class ParticipantInfo(
    val orderId: Long,
    val userId: Long,
    val memberNames: List<String>,
    val participantStatus: ParticipantStatusType,
    val deliveryMethod: String,
    val totalPrice: Int,
    val shippingInfo: ShippingInfo,
)
