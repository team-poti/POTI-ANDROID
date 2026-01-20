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
import com.poti.android.presentation.history.component.ParticipantStateLabelStage
import com.poti.android.presentation.history.component.ParticipantStateLabelStatus
import com.poti.android.presentation.history.model.PartySummaryUiModel
import com.poti.android.presentation.history.model.ProgressUiModel
import com.poti.android.presentation.history.model.participant.ActionButtonState
import com.poti.android.presentation.history.model.participant.DepositInfoUiModel
import com.poti.android.presentation.history.model.participant.DepositItemUiModel
import com.poti.android.presentation.history.model.participant.ParticipantDetailActionType
import com.poti.android.presentation.history.model.participant.ParticipantDetailModalUiModel
import com.poti.android.presentation.history.model.participant.ParticipantDetailUiModel
import com.poti.android.presentation.history.model.participant.ShippingInfoUiModel
import com.poti.android.presentation.history.model.recruiter.ParticipantInfoUiModel
import com.poti.android.presentation.history.model.recruiter.RecruiterDetailUiModel

fun RecruiterDetail.toUiModel(): RecruiterDetailUiModel {
    val uiArtistInfo = partySummary.toUiModel()
    val topBarTitleRes = if (uiArtistInfo.partyStatus == ParticipantStateLabelStatus.DONE &&
        uiArtistInfo.partyStage == ParticipantStateLabelStage.DELIVERY
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

    val topBarTitleResId = if (partyUiModel.partyStage == ParticipantStateLabelStage.DELIVERY &&
        partyUiModel.partyStatus == ParticipantStateLabelStatus.DONE
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

    val isTrackingInfoVisible = userStage == ParticipantStateLabelStage.DELIVERY &&
        userStatus == ParticipantStateLabelStatus.START

    val isParticipantStatusVisible = userStage == ParticipantStateLabelStage.DELIVERY

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
