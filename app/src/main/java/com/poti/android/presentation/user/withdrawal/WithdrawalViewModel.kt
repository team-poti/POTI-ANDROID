package com.poti.android.presentation.user.withdrawal

import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.network.model.NetworkError
import com.poti.android.domain.usecase.auth.GetWithdrawalReasonsUseCase
import com.poti.android.domain.usecase.auth.WithdrawalUseCase
import com.poti.android.presentation.user.withdrawal.model.WithdrawalUiEffect
import com.poti.android.presentation.user.withdrawal.model.WithdrawalUiIntent
import com.poti.android.presentation.user.withdrawal.model.WithdrawalUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

@HiltViewModel
class WithdrawalViewModel @Inject constructor(
    private val getWithdrawalReasonsUseCase: GetWithdrawalReasonsUseCase,
    private val withdrawalUseCase: WithdrawalUseCase,
) : BaseViewModel<WithdrawalUiState, WithdrawalUiIntent, WithdrawalUiEffect>(
        initialState = WithdrawalUiState(),
    ) {
    init {
        getWithdrawalReasons()
    }

    override fun processIntent(intent: WithdrawalUiIntent) {
        when (intent) {
            WithdrawalUiIntent.OnBackClick -> sendEffect(WithdrawalUiEffect.NavigateBack)
            is WithdrawalUiIntent.OnReasonSelect -> {
                updateState { copy(selectedReason = intent.reason) }
            }
            WithdrawalUiIntent.OnWithdrawalClick -> showWithdrawalModal()
            WithdrawalUiIntent.OnWithdrawalModalDismiss -> {
                updateState { copy(showWithdrawalModal = false) }
            }
            WithdrawalUiIntent.OnWithdrawalConfirmClick -> withdrawal()
            WithdrawalUiIntent.OnWithdrawalUnavailableModalClose -> {
                updateState { copy(showWithdrawalUnavailableModal = false) }
            }
        }
    }

    private fun getWithdrawalReasons() = launchScope {
        getWithdrawalReasonsUseCase()
            .onSuccess { reasons ->
                updateState {
                    copy(withdrawalReasons = ApiState.Success(reasons.toImmutableList()))
                }
            }
            .onFailure { error ->
                updateState {
                    copy(withdrawalReasons = ApiState.Failure(error.message ?: "탈퇴 사유 조회에 실패했습니다."))
                }
            }
    }

    private fun showWithdrawalModal() {
        if (!uiState.value.isWithdrawalEnabled) return

        updateState { copy(showWithdrawalModal = true) }
    }

    private fun withdrawal() {
        val reason = uiState.value.selectedReason?.code ?: return
        if (!uiState.value.isWithdrawalEnabled) return

        updateState {
            copy(
                withdrawalState = ApiState.Loading,
                showWithdrawalModal = false,
            )
        }

        launchScope(
            onError = ::handleWithdrawalFailure,
        ) {
            withdrawalUseCase(reason = reason)
                .onSuccess {
                    updateState { copy(withdrawalState = ApiState.Success(Unit)) }
                }
                .onFailure(::handleWithdrawalFailure)
        }
    }

    private fun handleWithdrawalFailure(error: Throwable) {
        val message = error.message ?: "회원탈퇴에 실패했습니다."
        val isWithdrawalUnavailable =
            error is NetworkError.BadRequest && error.code == WITHDRAWAL_UNAVAILABLE_CODE

        updateState {
            copy(
                withdrawalState = ApiState.Failure(message),
                showWithdrawalModal = false,
                showWithdrawalUnavailableModal = isWithdrawalUnavailable,
            )
        }

        if (!isWithdrawalUnavailable) {
            sendEffect(WithdrawalUiEffect.ShowError(message))
        }
    }

    private companion object {
        const val WITHDRAWAL_UNAVAILABLE_CODE = 40019
    }
}
