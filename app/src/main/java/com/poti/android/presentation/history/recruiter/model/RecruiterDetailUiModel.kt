package com.poti.android.presentation.history.recruiter.model

import com.poti.android.domain.model.history.ParticipantInfo
import com.poti.android.domain.model.history.PartySummary
import com.poti.android.domain.model.history.RecruiterDetail
import com.poti.android.domain.type.ParticipantStatusType

data class RecruiterDetailUiModel(
    val recruitId: Long,
    val orderNumber: String,
    val partySummary: PartySummary,
    val participants: List<ParticipantUiModel>,
    val participantCount: Int,
)

data class ParticipantUiModel(
    val orderId: Long,
    val userId: Long,
    val memberNamesString: String,
    val participantStatus: ParticipantStatusType,
    val shippingInfo: String,
    val deliveryMethod: String,
    val totalPrice: Int,
)

fun RecruiterDetail.toUiModel(): RecruiterDetailUiModel = RecruiterDetailUiModel(
    recruitId = this.recruitId,
    orderNumber = this.orderNumber,
    partySummary = this.partySummary,
    participants = this.participantInfoList.map { it.toUiModel() },
    participantCount = this.participantCount,
)

fun ParticipantInfo.toUiModel(): ParticipantUiModel {
    return ParticipantUiModel(
        orderId = this.orderId,
        userId = this.userId,
        memberNamesString = this.memberNames.joinToString(separator = ", "),
        participantStatus = this.participantStatus,
        shippingInfo = this.shippingInfo.run {
            "$receiverName\n$address\n$phone"
        },
        deliveryMethod = this.deliveryMethod,
        totalPrice = this.totalPrice,
    )
}
