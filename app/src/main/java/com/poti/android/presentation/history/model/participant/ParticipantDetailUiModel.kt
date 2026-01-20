package com.poti.android.presentation.history.model.participant

import androidx.annotation.StringRes
import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.component.ParticipantStateLabelStage
import com.poti.android.presentation.history.component.ParticipantStateLabelStatus
import com.poti.android.presentation.history.model.PartySummaryUiModel
import com.poti.android.presentation.history.model.ProgressUiModel

data class ParticipantDetailUiModel(
    val recruitId: Long,
    val userState: ParticipantStatusType,
    val partySummaryInfo: PartySummaryUiModel,
    val progressInfo: ProgressUiModel,
    val depositInfo: DepositInfoUiModel,
    val shippingInfo: ShippingInfoUiModel,
    val recruiterName: String,
    val recruiterProfileUrl: String,
    val recruiterRating: String,
    @StringRes val topBarTitleResId: Int,
    val actionButtonState: ActionButtonState,
    val activeModal: ParticipantDetailModalUiModel,
    val isTrackingInfoVisible: Boolean,
    val isParticipantStatusVisible: Boolean,
) {
    val userStage: ParticipantStateLabelStage = when (userState) {
        ParticipantStatusType.RECRUIT_WAIT -> ParticipantStateLabelStage.RECRUIT
        ParticipantStatusType.RECRUIT_DONE -> ParticipantStateLabelStage.RECRUIT
        ParticipantStatusType.DEPOSIT_WAIT -> ParticipantStateLabelStage.RECRUIT
        ParticipantStatusType.DEPOSIT_CHECK -> ParticipantStateLabelStage.DEPOSIT
        ParticipantStatusType.DEPOSIT_DONE -> ParticipantStateLabelStage.DEPOSIT
        ParticipantStatusType.DELIVERY_WAIT -> ParticipantStateLabelStage.DELIVERY
        ParticipantStatusType.DELIVERY_START -> ParticipantStateLabelStage.DELIVERY
        ParticipantStatusType.DELIVERY_DONE -> ParticipantStateLabelStage.DELIVERY
    }
    val userStatus: ParticipantStateLabelStatus = when (userState) {
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

sealed interface ActionButtonState {
    data object Gone : ActionButtonState

    data class Visible(
        @StringRes val textResId: Int,
        val actionType: ParticipantDetailActionType,
    ) : ActionButtonState
}

enum class ParticipantDetailActionType {
    OPEN_DEPOSIT_INPUT,
    OPEN_DELIVERY_CONFIRM,
}

sealed interface ParticipantDetailModalUiModel {
    data object None : ParticipantDetailModalUiModel

    data object DepositInput : ParticipantDetailModalUiModel

    data object DeliveryConfirm : ParticipantDetailModalUiModel

    data class DeliveryReview(
        val recruiterName: String,
        val recruiterProfileUrl: String,
        val recruiterRating: String,
    ) : ParticipantDetailModalUiModel
}

data class DepositInfoUiModel(
    val items: List<DepositItemUiModel>,
    val totalAmount: Int,
    val accountNumber: String?,
    val dueDate: String?,
    val stage: ParticipantStateLabelStage,
    val status: ParticipantStateLabelStatus,
)

data class DepositItemUiModel(
    val name: String,
    val price: Int,
    val type: PotiItemOptionType,
)

data class ShippingInfoUiModel(
    val recipient: String,
    val zipcode: String,
    val address: String,
    val phone: String,
    val deliveryMethod: String,
    val trackingNumber: String?,
)
