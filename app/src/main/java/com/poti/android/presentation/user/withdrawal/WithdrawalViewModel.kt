package com.poti.android.presentation.user.withdrawal

import com.poti.android.core.base.BaseViewModel
import com.poti.android.presentation.user.withdrawal.model.WithdrawalUiEffect
import com.poti.android.presentation.user.withdrawal.model.WithdrawalUiIntent
import com.poti.android.presentation.user.withdrawal.model.WithdrawalUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WithdrawalViewModel @Inject constructor() : BaseViewModel<WithdrawalUiState, WithdrawalUiIntent, WithdrawalUiEffect>(
    initialState = WithdrawalUiState(),
) {
    override fun processIntent(intent: WithdrawalUiIntent) {
        when (intent) {
            WithdrawalUiIntent.OnBackClick -> sendEffect(WithdrawalUiEffect.NavigateBack)
            is WithdrawalUiIntent.OnReasonSelect -> {
                updateState { copy(selectedReason = intent.reason) }
            }
        }
    }
}
