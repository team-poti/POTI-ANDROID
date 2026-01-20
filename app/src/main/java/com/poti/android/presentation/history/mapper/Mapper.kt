package com.poti.android.presentation.history.mapper

import com.poti.android.R
import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.domain.model.history.DepositItem
import com.poti.android.domain.model.history.DepositStatus
import com.poti.android.domain.model.history.ParticipantDetail
import com.poti.android.domain.model.history.ParticipantInfo
import com.poti.android.domain.model.history.ParticipantShippingInfo
import com.poti.android.domain.model.history.PartySummary
import com.poti.android.domain.model.history.ProgressInfo
import com.poti.android.domain.model.history.RecruiterDetail
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.component.StateLabelStage
import com.poti.android.presentation.history.component.StateLabelStatus
import com.poti.android.presentation.history.model.PartySummaryUiModel
import com.poti.android.presentation.history.model.ProgressUiModel
import com.poti.android.presentation.history.participant.model.ActionButtonState
import com.poti.android.presentation.history.participant.model.DepositInfoUiModel
import com.poti.android.presentation.history.participant.model.DepositItemUiModel
import com.poti.android.presentation.history.participant.model.ParticipantDetailActionType
import com.poti.android.presentation.history.participant.model.ParticipantDetailModalUiModel
import com.poti.android.presentation.history.participant.model.ParticipantDetailUiModel
import com.poti.android.presentation.history.participant.model.ShippingInfoUiModel
import com.poti.android.presentation.history.recruiter.model.ParticipantInfoUiModel
import com.poti.android.presentation.history.recruiter.model.RecruiterDetailUiModel

fun RecruiterDetail.toUiModel(): RecruiterDetailUiModel {
    val uiArtistInfo = partySummary.toUiModel()
    val topBarTitleRes = if (uiArtistInfo.partyStatus == StateLabelStatus.DONE &&
        uiArtistInfo.partyStage == StateLabelStage.DELIVERY
    ) {
        R.string.history_ongoing_title_done
    } else {
        R.string.history_ongoing_title
    }

    return RecruiterDetailUiModel(
        recruitId = recruitId,
        artistInfo = uiArtistInfo,
        progressInfo = progressInfo.toUiModel(),
        participantInfoList = participantInfoList.map { it.toUiModel() },
        participantCount = participantCount,
        topBarTitleRes = topBarTitleRes,
    )
}

fun ParticipantInfo.toUiModel(): ParticipantInfoUiModel {
    return ParticipantInfoUiModel(
        userId = userId,
        memberNames = memberNames,
        userInfo = userInfo,
        deliveryMethod = deliveryMethod,
        deliveryPrice = deliveryPrice,
        participantState = participantState,
    )
}

fun ParticipantDetail.toUiModel(): ParticipantDetailUiModel {
    val (depositStage, depositStatus) = depositInfo.depositStatus.toUiState()
    val partyUiModel = partySummary.toUiModel()

    val topBarTitleResId = if (partyUiModel.partyStage == StateLabelStage.DELIVERY &&
        partyUiModel.partyStatus == StateLabelStatus.DONE
    ) {
        R.string.history_participant_detail_title_done
    } else {
        R.string.history_participant_detail_title
    }

    val actionButtonState = when (userState) {
        ParticipantStatusType.RECRUIT_DONE -> ActionButtonState.Visible(
            textResId = R.string.history_deposit_done_button,
            actionType = ParticipantDetailActionType.OPEN_DEPOSIT_INPUT,
        )
        ParticipantStatusType.DELIVERY_START -> ActionButtonState.Visible(
            textResId = R.string.history_delivery_done_button,
            actionType = ParticipantDetailActionType.OPEN_DELIVERY_CONFIRM,
        )
        else -> ActionButtonState.Gone
    }

    val (userStage, userStatus) = userState.toUiState()

    val isTrackingInfoVisible = userStage == StateLabelStage.DELIVERY &&
        userStatus == StateLabelStatus.START

    val isParticipantStatusVisible = userStage == StateLabelStage.DELIVERY

    return ParticipantDetailUiModel(
        recruitId = recruitId,
        userState = userState,
        partySummaryInfo = partyUiModel,
        progressInfo = progressInfo.toUiModel(),
        depositInfo = DepositInfoUiModel(
            items = depositInfo.items.map { it.toUiModel() },
            totalAmount = depositInfo.totalAmount,
            accountNumber = depositInfo.depositStatus.accountNumber.takeIf { it.isNotEmpty() },
            dueDate = depositInfo.depositStatus.dueDate.takeIf { it.isNotEmpty() },
            stage = depositStage,
            status = depositStatus,
        ),
        shippingInfo = shippingInfo.toUiModel(),
        recruiterName = recruiterName,
        recruiterProfileUrl = recruiterProfileUrl,
        recruiterRating = recruiterRating,
        topBarTitleResId = topBarTitleResId,
        actionButtonState = actionButtonState,
        activeModal = ParticipantDetailModalUiModel.None,
        isTrackingInfoVisible = isTrackingInfoVisible,
        isParticipantStatusVisible = isParticipantStatusVisible,
    )
}

fun PartySummary.toUiModel(): PartySummaryUiModel {
    val (stage, status) = partyState.toUiState()
    return PartySummaryUiModel(
        imageUrl = imageUrl,
        artist = artist,
        title = title,
        partyStage = stage,
        partyStatus = status,
    )
}

fun ProgressInfo.toUiModel(): ProgressUiModel {
    val guideText = when (step) {
        0 -> "모집이 시작되었습니다."
        1 -> "입금을 확인하고 있습니다."
        2 -> "배송이 시작되었습니다."
        3 -> "거래가 완료되었습니다."
        else -> ""
    }
    return ProgressUiModel(
        guideText = guideText,
        step = step,
    )
}

fun DepositItem.toUiModel(): DepositItemUiModel {
    val type = when (this) {
        is DepositItem.DeliveryItem -> PotiItemOptionType.DELIVERY
        is DepositItem.MemberItem -> PotiItemOptionType.MEMBER
    }
    return DepositItemUiModel(
        name = name,
        price = price,
        type = type,
    )
}

fun ParticipantShippingInfo.toUiModel(): ShippingInfoUiModel {
    return ShippingInfoUiModel(
        recipient = recipient,
        zipcode = zipcode,
        address = address,
        phone = phone,
        deliveryMethod = deliveryMethod,
        trackingNumber = trackingNumber,
    )
}

fun ParticipantStatusType.toUiState(): Pair<StateLabelStage, StateLabelStatus> {
    return when (this) {
        ParticipantStatusType.RECRUIT_ING ->
            StateLabelStage.RECRUIT to StateLabelStatus.ING
        ParticipantStatusType.RECRUIT_DONE ->
            StateLabelStage.RECRUIT to StateLabelStatus.DONE

        ParticipantStatusType.DEPOSIT_WAIT ->
            StateLabelStage.DEPOSIT to StateLabelStatus.WAIT
        ParticipantStatusType.DEPOSIT_CHECK ->
            StateLabelStage.DEPOSIT to StateLabelStatus.CHECK
        ParticipantStatusType.DEPOSIT_DONE ->
            StateLabelStage.DEPOSIT to StateLabelStatus.DONE

        ParticipantStatusType.DELIVERY_WAIT ->
            StateLabelStage.DELIVERY to StateLabelStatus.WAIT
        ParticipantStatusType.DELIVERY_START ->
            StateLabelStage.DELIVERY to StateLabelStatus.START
        ParticipantStatusType.DELIVERY_DONE ->
            StateLabelStage.DELIVERY to StateLabelStatus.DONE
    }
}

fun DepositStatus.toUiState(): Pair<StateLabelStage, StateLabelStatus> {
    return when (this) {
        is DepositStatus.DepositWait ->
            StateLabelStage.DEPOSIT to StateLabelStatus.WAIT
        is DepositStatus.DepositCheck ->
            StateLabelStage.DEPOSIT to StateLabelStatus.CHECK
        DepositStatus.DepositDone ->
            StateLabelStage.DEPOSIT to StateLabelStatus.DONE
    }
}
