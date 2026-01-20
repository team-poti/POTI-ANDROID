package com.poti.android.presentation.history.participant.model

import androidx.annotation.StringRes
import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.component.StateLabelStage
import com.poti.android.presentation.history.component.StateLabelStatus
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
)

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
    val stage: StateLabelStage,
    val status: StateLabelStatus,
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
