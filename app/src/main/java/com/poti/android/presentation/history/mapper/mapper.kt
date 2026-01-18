package com.poti.android.presentation.history.mapper

import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.domain.model.history.DepositItem
import com.poti.android.domain.model.history.DepositStatus
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.component.ParticipantStateLabelStage
import com.poti.android.presentation.history.component.ParticipantStateLabelStatus

fun ParticipantStatusType.toUiState(): Pair<ParticipantStateLabelStage, ParticipantStateLabelStatus> {
    return when (this) {
        ParticipantStatusType.RECRUIT_WAIT ->
            ParticipantStateLabelStage.RECRUIT to ParticipantStateLabelStatus.WAIT
        ParticipantStatusType.RECRUIT_DONE ->
            ParticipantStateLabelStage.RECRUIT to ParticipantStateLabelStatus.DONE

        ParticipantStatusType.DEPOSIT_WAIT ->
            ParticipantStateLabelStage.DEPOSIT to ParticipantStateLabelStatus.WAIT
        ParticipantStatusType.DEPOSIT_CHECK ->
            ParticipantStateLabelStage.DEPOSIT to ParticipantStateLabelStatus.CHECK
        ParticipantStatusType.DEPOSIT_DONE ->
            ParticipantStateLabelStage.DEPOSIT to ParticipantStateLabelStatus.DONE

        ParticipantStatusType.DELIVERY_WAIT ->
            ParticipantStateLabelStage.DELIVERY to ParticipantStateLabelStatus.WAIT
        ParticipantStatusType.DELIVERY_START ->
            ParticipantStateLabelStage.DELIVERY to ParticipantStateLabelStatus.START
        ParticipantStatusType.DELIVERY_DONE ->
            ParticipantStateLabelStage.DELIVERY to ParticipantStateLabelStatus.DONE
    }
}

fun DepositItem.toUiState() = when (this) {
    is DepositItem.DeliveryItem -> PotiItemOptionType.DELIVERY
    is DepositItem.MemberItem -> PotiItemOptionType.MEMBER
}

fun DepositStatus.toUiState(): Pair<ParticipantStateLabelStage, ParticipantStateLabelStatus> {
    return when (this) {
        is DepositStatus.DepositWait ->
            ParticipantStateLabelStage.DEPOSIT to ParticipantStateLabelStatus.WAIT
        is DepositStatus.DepositCheck ->
            ParticipantStateLabelStage.DEPOSIT to ParticipantStateLabelStatus.CHECK
        DepositStatus.DepositDone ->
            ParticipantStateLabelStage.DEPOSIT to ParticipantStateLabelStatus.DONE
    }
}
