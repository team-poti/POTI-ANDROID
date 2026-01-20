package com.poti.android.presentation.history.model.recruiter

import androidx.annotation.StringRes
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.component.StateLabelStage
import com.poti.android.presentation.history.component.StateLabelStatus
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
    val stage: StateLabelStage = when (participantState) {
        ParticipantStatusType.RECRUIT_ING -> StateLabelStage.RECRUIT
        ParticipantStatusType.RECRUIT_DONE -> StateLabelStage.RECRUIT
        ParticipantStatusType.DEPOSIT_WAIT -> StateLabelStage.RECRUIT
        ParticipantStatusType.DEPOSIT_CHECK -> StateLabelStage.DEPOSIT
        ParticipantStatusType.DEPOSIT_DONE -> StateLabelStage.DEPOSIT
        ParticipantStatusType.DELIVERY_WAIT -> StateLabelStage.DELIVERY
        ParticipantStatusType.DELIVERY_START -> StateLabelStage.DELIVERY
        ParticipantStatusType.DELIVERY_DONE -> StateLabelStage.DELIVERY
    }
    val status: StateLabelStatus = when (participantState) {
        ParticipantStatusType.RECRUIT_ING -> StateLabelStatus.ING
        ParticipantStatusType.RECRUIT_DONE -> StateLabelStatus.DONE
        ParticipantStatusType.DEPOSIT_WAIT -> StateLabelStatus.WAIT
        ParticipantStatusType.DEPOSIT_CHECK -> StateLabelStatus.CHECK
        ParticipantStatusType.DEPOSIT_DONE -> StateLabelStatus.DONE
        ParticipantStatusType.DELIVERY_WAIT -> StateLabelStatus.WAIT
        ParticipantStatusType.DELIVERY_START -> StateLabelStatus.START
        ParticipantStatusType.DELIVERY_DONE -> StateLabelStatus.DONE
    }
}
