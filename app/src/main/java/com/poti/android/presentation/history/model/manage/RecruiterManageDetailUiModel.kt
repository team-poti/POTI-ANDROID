package com.poti.android.presentation.history.model.manage

import com.poti.android.presentation.history.component.DepositItem
import com.poti.android.presentation.history.component.ParticipantStateLabelStage
import com.poti.android.presentation.history.component.ParticipantStateLabelStatus

data class RecruiterManageDetailUiModel(
    val participantId: Long,
    val nickname: String,
    val profileImage: String,
    val stage: ParticipantStateLabelStage,
    val status: ParticipantStateLabelStatus,
    val depositItems: List<DepositItem>,
    val depositTotalPrice: Int,
    val detailState: RecruiterManageStateUiModel,
)

sealed interface RecruiterManageStateUiModel {
    data object Default : RecruiterManageStateUiModel

    data class DepositCheck(
        val deposit: String,
    ) : RecruiterManageStateUiModel

    data class Delivery(
        val name: String,
        val delivery: String,
        val contact: String,
    ) : RecruiterManageStateUiModel

    data class AfterDelivery(
        val name: String,
        val delivery: String,
        val contact: String,
        val invoice: String,
    ) : RecruiterManageStateUiModel

    data class Finished(
        val invoice: String,
    ) : RecruiterManageStateUiModel
}
