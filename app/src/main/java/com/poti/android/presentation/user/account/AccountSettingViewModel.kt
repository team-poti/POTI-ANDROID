package com.poti.android.presentation.user.account

import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.usecase.auth.LogoutUseCase
import com.poti.android.domain.usecase.user.GetUserAccountUseCase
import com.poti.android.presentation.user.account.model.AccountSettingUiEffect
import com.poti.android.presentation.user.account.model.AccountSettingUiIntent
import com.poti.android.presentation.user.account.model.AccountSettingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountSettingViewModel @Inject constructor(
    private val getUserAccountUseCase: GetUserAccountUseCase,
    private val logoutUseCase: LogoutUseCase,
) : BaseViewModel<AccountSettingUiState, AccountSettingUiIntent, AccountSettingUiEffect>(
        initialState = AccountSettingUiState(),
    ) {
    override fun processIntent(intent: AccountSettingUiIntent) {
        when (intent) {
            AccountSettingUiIntent.OnBackClick -> sendEffect(AccountSettingUiEffect.NavigateBack)
            AccountSettingUiIntent.OnWithdrawalClick -> sendEffect(AccountSettingUiEffect.NavigateToWithdrawal)
            AccountSettingUiIntent.OnLogoutClick -> logout()
        }
    }

    init {
        loadUserAccount()
    }

    private fun loadUserAccount() {
        updateState { copy(userAccountLoadState = ApiState.Loading) }
        launchScope(
            onError = { error ->
                updateState { copy(userAccountLoadState = ApiState.Failure(error.message ?: "Failed")) }
            },
        ) {
            getUserAccountUseCase()
                .onSuccess { userAccount ->
                    updateState {
                        copy(userAccountLoadState = ApiState.Success(userAccount))
                    }
                }
                .onFailure { throwable ->
                    updateState {
                        copy(userAccountLoadState = ApiState.Failure(throwable.message ?: "Failed"))
                    }
                }
        }
    }

    private fun logout() {
        if (uiState.value.isLoggingOut) return

        updateState { copy(isLoggingOut = true) }
        launchScope(
            onError = { updateState { copy(isLoggingOut = false) } },
        ) {
            logoutUseCase()
                .onFailure { updateState { copy(isLoggingOut = false) } }
        }
    }
}
