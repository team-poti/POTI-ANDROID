package com.poti.android.presentation.history.model.recruiter

import androidx.annotation.StringRes
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.component.ParticipantStateLabelStage
import com.poti.android.presentation.history.component.ParticipantStateLabelStatus
import com.poti.android.presentation.history.model.PartySummaryUiModel
import com.poti.android.presentation.history.model.ProgressUiModel

data class RecruiterDetailUiModel(
    val recruitId: Long,
    val artistInfo: PartySummaryUiModel,
    val progressInfo: ProgressUiModel,
    val participantInfoList: List<ParticipantInfoUiModel>,
    val participantCount: Int,
    @StringRes val topBarTitleRes: Int,
)

data class ParticipantInfoUiModel(
    val userId: Long,
    val memberNames: String,
    val userInfo: String,
    val deliveryMethod: String,
    val deliveryPrice: Int,
    val participantState: ParticipantStatusType,
) {
    val stage: ParticipantStateLabelStage = when (participantState) {
        ParticipantStatusType.RECRUIT_WAIT -> ParticipantStateLabelStage.RECRUIT
        ParticipantStatusType.RECRUIT_DONE -> ParticipantStateLabelStage.RECRUIT
        ParticipantStatusType.DEPOSIT_WAIT -> ParticipantStateLabelStage.RECRUIT
        ParticipantStatusType.DEPOSIT_CHECK -> ParticipantStateLabelStage.DEPOSIT
        ParticipantStatusType.DEPOSIT_DONE -> ParticipantStateLabelStage.DEPOSIT
        ParticipantStatusType.DELIVERY_WAIT -> ParticipantStateLabelStage.DELIVERY
        ParticipantStatusType.DELIVERY_START -> ParticipantStateLabelStage.DELIVERY
        ParticipantStatusType.DELIVERY_DONE -> ParticipantStateLabelStage.DELIVERY
    }
    val status: ParticipantStateLabelStatus = when (participantState) {
        ParticipantStatusType.RECRUIT_WAIT -> ParticipantStateLabelStatus.WAIT
        ParticipantStatusType.RECRUIT_DONE -> ParticipantStateLabelStatus.DONE
        ParticipantStatusType.DEPOSIT_WAIT -> ParticipantStateLabelStatus.WAIT
        ParticipantStatusType.DEPOSIT_CHECK -> ParticipantStateLabelStatus.CHECK
        ParticipantStatusType.DEPOSIT_DONE -> ParticipantStateLabelStatus.DONE
        ParticipantStatusType.DELIVERY_WAIT -> ParticipantStateLabelStatus.WAIT
        ParticipantStatusType.DELIVERY_START -> ParticipantStateLabelStatus.START
        ParticipantStatusType.DELIVERY_DONE -> ParticipantStateLabelStatus.DONE
    }
}
