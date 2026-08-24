package com.poti.android.presentation.user.account.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.user.UserAccount

data class AccountSettingUiState(
    val userAccountLoadState: ApiState<UserAccount> = ApiState.Loading,
    val isLoggingOut: Boolean = false,
) : UiState

sealed interface AccountSettingUiIntent : UiIntent {
    data object OnBackClick : AccountSettingUiIntent

    data object OnLogoutClick : AccountSettingUiIntent

    data object OnWithdrawalClick : AccountSettingUiIntent
}

sealed interface AccountSettingUiEffect : UiEffect {
    data object NavigateBack : AccountSettingUiEffect

    data object NavigateToWithdrawal : AccountSettingUiEffect
}
