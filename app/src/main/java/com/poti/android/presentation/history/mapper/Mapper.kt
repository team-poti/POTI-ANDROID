package com.poti.android.presentation.history.mapper

import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.domain.model.history.ParticipantManageDetail
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.component.ParticipantStateLabelStage
import com.poti.android.presentation.history.component.ParticipantStateLabelStatus
import com.poti.android.presentation.history.model.manage.RecruiterManageDetailUiModel
import com.poti.android.presentation.history.model.manage.RecruiterManageStateUiModel
import com.poti.android.presentation.history.component.DepositItem as ComponentDepositItem

fun ParticipantManageDetail.toUiModel(): RecruiterManageDetailUiModel {
    val (stage, status) = participantState.toUiState()

    val depositItems = listOf(
        ComponentDepositItem(
            type = PotiItemOptionType.MEMBER,
            name = selectedMember,
            price = memberPrice,
        ),
        ComponentDepositItem(
            type = PotiItemOptionType.DELIVERY,
            name = deliveryMethod,
            price = deliveryPrice,
        ),
    )

    val detailState = when (participantState) {
        ParticipantStatusType.DEPOSIT_CHECK -> {
            RecruiterManageStateUiModel.DepositCheck(
                deposit = "입금자명 ${depositorName ?: ""} / ${depositTime ?: ""}",
            )
        }
        ParticipantStatusType.DELIVERY_WAIT -> {
            RecruiterManageStateUiModel.Delivery(
                name = recipient ?: "",
                delivery = "(${zipcode ?: ""}) ${address ?: ""}",
                contact = phoneNumber ?: "",
            )
        }
        ParticipantStatusType.DELIVERY_START, ParticipantStatusType.DELIVERY_DONE -> {
            RecruiterManageStateUiModel.AfterDelivery(
                name = recipient ?: "",
                delivery = "(${zipcode ?: ""}) ${address ?: ""}",
                contact = phoneNumber ?: "",
                invoice = "$deliveryMethod ${trackingNumber ?: ""}",
            )
        }
        else -> RecruiterManageStateUiModel.Default
    }

    return RecruiterManageDetailUiModel(
        participantId = participantId,
        nickname = nickname,
        profileImage = profileImage ?: "",
        stage = stage,
        status = status,
        depositItems = depositItems,
        depositTotalPrice = memberPrice + deliveryPrice,
        detailState = detailState,
    )
}

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
