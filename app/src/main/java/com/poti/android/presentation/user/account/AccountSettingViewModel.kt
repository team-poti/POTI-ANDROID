package com.poti.android.presentation.user.account

import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.usecase.user.GetUserAccountUseCase
import com.poti.android.presentation.user.account.model.AccountSettingUiEffect
import com.poti.android.presentation.user.account.model.AccountSettingUiIntent
import com.poti.android.presentation.user.account.model.AccountSettingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountSettingViewModel @Inject constructor(
    private val getUserAccountUseCase: GetUserAccountUseCase,
) : BaseViewModel<AccountSettingUiState, AccountSettingUiIntent, AccountSettingUiEffect>(
        initialState = AccountSettingUiState(),
    ) {
    override fun processIntent(intent: AccountSettingUiIntent) {
        when (intent) {
            AccountSettingUiIntent.OnBackClick -> sendEffect(AccountSettingUiEffect.NavigateBack)
            AccountSettingUiIntent.OnWithdrawalClick -> sendEffect(AccountSettingUiEffect.NavigateToWithdrawal)
            // TODO: 사용자가 직접 트리거하는 로그아웃 API/유즈케이스가 아직 없음
            AccountSettingUiIntent.OnLogoutClick -> {}
        }
    }

    init {
        loadUserAccount()
    }

    private fun loadUserAccount() = launchScope {
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
