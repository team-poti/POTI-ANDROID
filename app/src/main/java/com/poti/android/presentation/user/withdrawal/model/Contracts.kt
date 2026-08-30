package com.poti.android.presentation.user.withdrawal.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.type.WithdrawalReasonType

data class WithdrawalUiState(
    val selectedReason: WithdrawalReasonType? = null,
    val withdrawalState: ApiState<Unit> = ApiState.Init,
    val showWithdrawalModal: Boolean = false,
    val showWithdrawalUnavailableModal: Boolean = false,
) : UiState {
    val isWithdrawalEnabled: Boolean
        get() = selectedReason != null &&
            withdrawalState !is ApiState.Loading &&
            withdrawalState !is ApiState.Success
}

sealed interface WithdrawalUiIntent : UiIntent {
    data object OnBackClick : WithdrawalUiIntent

    data class OnReasonSelect(
        val reason: WithdrawalReasonType,
    ) : WithdrawalUiIntent

    data object OnWithdrawalClick : WithdrawalUiIntent

    data object OnWithdrawalModalDismiss : WithdrawalUiIntent

    data object OnWithdrawalConfirmClick : WithdrawalUiIntent

    data object OnWithdrawalUnavailableModalClose : WithdrawalUiIntent
}

sealed interface WithdrawalUiEffect : UiEffect {
    data object NavigateBack : WithdrawalUiEffect

    data class ShowError(
        val message: String,
    ) : WithdrawalUiEffect
}
