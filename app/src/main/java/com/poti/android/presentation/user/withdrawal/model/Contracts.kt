package com.poti.android.presentation.user.withdrawal.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.domain.type.WithdrawalReasonType

data class WithdrawalUiState(
    val selectedReason: WithdrawalReasonType? = null,
) : UiState {
    val isWithdrawalEnabled: Boolean
        get() = selectedReason != null
}

sealed interface WithdrawalUiIntent : UiIntent {
    data object OnBackClick : WithdrawalUiIntent

    data class OnReasonSelect(
        val reason: WithdrawalReasonType,
    ) : WithdrawalUiIntent
}

sealed interface WithdrawalUiEffect : UiEffect {
    data object NavigateBack : WithdrawalUiEffect
}
