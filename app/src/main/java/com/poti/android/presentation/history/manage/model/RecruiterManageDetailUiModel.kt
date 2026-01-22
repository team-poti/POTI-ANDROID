package com.poti.android.presentation.history.manage.model

import com.poti.android.domain.model.history.DepositInfo
import com.poti.android.domain.model.history.MemberPriceInfo
import com.poti.android.domain.model.history.ParticipantDetailInfo
import com.poti.android.domain.model.history.ParticipantManageDetail
import com.poti.android.domain.model.history.ShippingInfo
import com.poti.android.domain.type.ParticipantStatusType

data class RecruiterManageDetailUiModel(
    val participants: List<ParticipantUiModel>,
)

data class ParticipantUiModel(
    val orderId: Long,
    val userId: Long,
    val profileImage: String?,
    val nickname: String,
    val participantStatus: ParticipantStatusType,
    val memberNames: String,
    val priceInfo: List<MemberPriceInfo>,
    val shippingName: String,
    val shippingPrice: Int,
    val totalPrice: Int,
    val depositInfo: DepositInfo?,
    val shippingInfo: ShippingInfo?,
)

fun ParticipantManageDetail.toUiModel(): RecruiterManageDetailUiModel {
    return RecruiterManageDetailUiModel(
        participants = this.participants.map { it.toUiModel() },
    )
}

fun ParticipantDetailInfo.toUiModel(): ParticipantUiModel {
    return ParticipantUiModel(
        orderId = this.orderId,
        userId = this.userId,
        profileImage = this.profileImage,
        nickname = this.nickname,
        participantStatus = this.participantStatus,
        memberNames = this.memberNames.joinToString(separator = ", "),
        priceInfo = this.priceInfo,
        shippingName = this.shippingName,
        shippingPrice = this.shippingPrice,
        totalPrice = this.totalPrice,
        depositInfo = this.depositInfo,
        shippingInfo = this.shippingInfo,
    )
}
