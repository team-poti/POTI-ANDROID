package com.poti.android.presentation.history.model.manage

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState

data class ParticipantManageUiState(
    val participantManageDetail: ApiState<List<RecruiterManageDetailUiModel>> = ApiState.Loading,
    val activeModal: ManageModalState = ManageModalState.None,
) : UiState

sealed interface ManageModalState {
    data object None : ManageModalState

    data class DepositConfirm(val participantId: Long) : ManageModalState

    data class DeliveryInput(val participantId: Long) : ManageModalState
}

sealed interface ParticipantManageUiIntent : UiIntent {
    data object OnBackClick : ParticipantManageUiIntent

    data class OnDepositConfirmClick(val participantId: Long) : ParticipantManageUiIntent

    data class OnDeliveryInputClick(val participantId: Long) : ParticipantManageUiIntent

    data object DismissModal : ParticipantManageUiIntent

    data class ConfirmDeposit(val participantId: Long) : ParticipantManageUiIntent

    data class RegisterDelivery(
        val participantId: Long,
        val deliveryMethod: String,
        val trackingNumber: String,
    ) : ParticipantManageUiIntent
}

sealed interface ParticipantManageUiEffect : UiEffect {
    data object NavigateBack : ParticipantManageUiEffect
}
