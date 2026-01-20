package com.poti.android.presentation.history

import com.poti.android.R
import com.poti.android.core.designsystem.component.display.PotiItemOptionType
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

object DummyParticipantManageDetail {
    // ========================================================================
    // 1. Shared Common Data
    // ========================================================================

    private val commonPartySummary = PartySummaryUiModel(
        artist = "NewJeans",
        title = "How Sweet 위버스 특전 분철",
        imageUrl = "https://picsum.photos/200",
        partyStage = ParticipantStateLabelStage.RECRUIT,
        partyStatus = ParticipantStateLabelStatus.DONE,
    )

    private val commonDepositItems = listOf(
        DepositItemUiModel(name = "해린 포토카드", price = 15000, type = PotiItemOptionType.MEMBER),
        DepositItemUiModel(name = "GS25 반값택배", price = 1800, type = PotiItemOptionType.DELIVERY),
    )

    private val commonShippingInfo = ShippingInfoUiModel(
        recipient = "김포티",
        zipcode = "06000",
        address = "서울특별시 강남구 테헤란로 123 포티타워 101호",
        phone = "010-1234-5678",
        deliveryMethod = "GS25 반값택배",
        trackingNumber = null,
    )

    // ========================================================================
    // 2. Recruiter View Data
    // ========================================================================

    val recruiterRecruitStep = RecruiterDetailUiModel(
        recruitId = 1,
        artistInfo = PartySummaryUiModel(
            imageUrl = "",
            artist = "IVE(아이브)",
            title = "I've IVE 위드뮤 분철",
            partyStage = ParticipantStateLabelStage.RECRUIT,
            partyStatus = ParticipantStateLabelStatus.WAIT,
        ),
        progressInfo = ProgressUiModel(
            step = 0,
            guideText = "모집이 시작되었습니다.",
        ),
        participantInfoList = listOf(
            ParticipantInfoUiModel(
                userId = 101,
                memberNames = "장원영",
                participantState = ParticipantStatusType.RECRUIT_DONE,
                userInfo = "포티",
                deliveryMethod = "GS반값택배",
                deliveryPrice = 1800,
            ),
        ),
        participantCount = 1,
        topBarTitleRes = R.string.history_ongoing_title,
    )

    val recruiterDepositStep = RecruiterDetailUiModel(
        recruitId = 2,
        artistInfo = PartySummaryUiModel(
            imageUrl = "",
            artist = "aespa(에스파)",
            title = "Armageddon 미공포 분철",
            partyStage = ParticipantStateLabelStage.DEPOSIT,
            partyStatus = ParticipantStateLabelStatus.WAIT,
        ),
        progressInfo = ProgressUiModel(
            step = 1,
            guideText = "입금을 확인하고 있습니다.",
        ),
        participantInfoList = listOf(
            ParticipantInfoUiModel(
                userId = 201,
                memberNames = "카리나, 윈터",
                participantState = ParticipantStatusType.DEPOSIT_DONE,
                userInfo = "김철수",
                deliveryMethod = "CU끼리택배",
                deliveryPrice = 1600,
            ),
            ParticipantInfoUiModel(
                userId = 202,
                memberNames = "닝닝",
                participantState = ParticipantStatusType.DEPOSIT_WAIT,
                userInfo = "이영희",
                deliveryMethod = "준등기",
                deliveryPrice = 1800,
            ),
            ParticipantInfoUiModel(
                userId = 203,
                memberNames = "지젤",
                participantState = ParticipantStatusType.DEPOSIT_CHECK,
                userInfo = "박민수",
                deliveryMethod = "일반택배",
                deliveryPrice = 3500,
            ),
        ),
        participantCount = 3,
        topBarTitleRes = R.string.history_ongoing_title,
    )

    val recruiterDeliveryDoneStep = RecruiterDetailUiModel(
        recruitId = 3,
        artistInfo = PartySummaryUiModel(
            imageUrl = "",
            artist = "NewJeans",
            title = "How Sweet 위버스 특전",
            partyStage = ParticipantStateLabelStage.DELIVERY,
            partyStatus = ParticipantStateLabelStatus.DONE,
        ),
        progressInfo = ProgressUiModel(
            step = 2,
            guideText = "거래가 완료되었습니다.",
        ),
        participantInfoList = listOf(
            ParticipantInfoUiModel(
                userId = 301,
                memberNames = "민지, 하니",
                participantState = ParticipantStatusType.DELIVERY_DONE,
                userInfo = "최예나",
                deliveryMethod = "GS반값택배",
                deliveryPrice = 1800,
            ),
            ParticipantInfoUiModel(
                userId = 302,
                memberNames = "다니엘",
                participantState = ParticipantStatusType.DELIVERY_DONE,
                userInfo = "조유리",
                deliveryMethod = "GS반값택배",
                deliveryPrice = 1800,
            ),
        ),
        participantCount = 2,
        topBarTitleRes = R.string.history_ongoing_title_done,
    )

    // ========================================================================
    // 3. Participant View Data
    // ========================================================================

    val participantDetailWaitDeposit = ParticipantDetailUiModel(
        recruitId = 1001L,
        userState = ParticipantStatusType.RECRUIT_DONE,
        partySummaryInfo = commonPartySummary,
        progressInfo = ProgressUiModel(
            step = 1,
            guideText = "입금을 확인하고 있습니다.",
        ),
        depositInfo = DepositInfoUiModel(
            items = commonDepositItems,
            totalAmount = 16800,
            accountNumber = "카카오뱅크 3333-01-1234567",
            dueDate = "2024.12.31 23:59까지",
            stage = ParticipantStateLabelStage.DEPOSIT,
            status = ParticipantStateLabelStatus.WAIT,
        ),
        shippingInfo = commonShippingInfo,
        recruiterName = "포티 총대",
        recruiterProfileUrl = "https://picsum.photos/id/64/200",
        recruiterRating = "4.8",
        topBarTitleResId = R.string.history_participant_detail_title,
        actionButtonState = ActionButtonState.Visible(
            textResId = R.string.history_deposit_done_button,
            actionType = ParticipantDetailActionType.OPEN_DEPOSIT_INPUT,
        ),
        activeModal = ParticipantDetailModalUiModel.None,
        isTrackingInfoVisible = false,
        isParticipantStatusVisible = false,
    )

    val participantDetailCheckDeposit = ParticipantDetailUiModel(
        recruitId = 1002L,
        userState = ParticipantStatusType.DEPOSIT_CHECK,
        partySummaryInfo = commonPartySummary,
        progressInfo = ProgressUiModel(
            step = 1,
            guideText = "입금을 확인하고 있습니다.",
        ),
        depositInfo = DepositInfoUiModel(
            items = commonDepositItems,
            totalAmount = 16800,
            accountNumber = "카카오뱅크 3333-01-1234567",
            dueDate = "2024.12.31 23:59까지",
            stage = ParticipantStateLabelStage.DEPOSIT,
            status = ParticipantStateLabelStatus.CHECK,
        ),
        shippingInfo = commonShippingInfo,
        recruiterName = "포티 총대",
        recruiterProfileUrl = "https://picsum.photos/id/64/200",
        recruiterRating = "4.8",
        topBarTitleResId = R.string.history_participant_detail_title,
        actionButtonState = ActionButtonState.Gone,
        activeModal = ParticipantDetailModalUiModel.None,
        isTrackingInfoVisible = false,
        isParticipantStatusVisible = false,
    )

    val participantDetailDeliveryStart = ParticipantDetailUiModel(
        recruitId = 1003L,
        userState = ParticipantStatusType.DELIVERY_START,
        partySummaryInfo = commonPartySummary.copy(
            partyStage = ParticipantStateLabelStage.DELIVERY,
            partyStatus = ParticipantStateLabelStatus.START,
        ),
        progressInfo = ProgressUiModel(
            step = 2,
            guideText = "배송이 시작되었습니다.",
        ),
        depositInfo = DepositInfoUiModel(
            items = commonDepositItems,
            totalAmount = 16800,
            accountNumber = null,
            dueDate = null,
            stage = ParticipantStateLabelStage.DEPOSIT,
            status = ParticipantStateLabelStatus.DONE,
        ),
        shippingInfo = commonShippingInfo.copy(
            trackingNumber = "1234-5678-9012",
        ),
        recruiterName = "포티 총대",
        recruiterProfileUrl = "https://picsum.photos/id/64/200",
        recruiterRating = "4.8",
        topBarTitleResId = R.string.history_participant_detail_title,
        actionButtonState = ActionButtonState.Visible(
            textResId = R.string.history_delivery_done_button,
            actionType = ParticipantDetailActionType.OPEN_DELIVERY_CONFIRM,
        ),
        activeModal = ParticipantDetailModalUiModel.None,
        isTrackingInfoVisible = true,
        isParticipantStatusVisible = true,
    )
}
