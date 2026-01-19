package com.poti.android.domain.model.history

import com.poti.android.domain.type.ParticipantStatusType

data class ParticipantManageDetail(
    val participantId: Long,
    val nickname: String,
    val profileImage: String?,
    val participantState: ParticipantStatusType,
    val selectedMember: String,
    val memberPrice: Int,
    val deliveryMethod: String,
    val deliveryPrice: Int,
    val depositTime: String?,
    val depositorName: String?,
    val recipient: String?,
    val phoneNumber: String?,
    val zipcode: String?,
    val address: String?,
    val trackingNumber: String?,
)
