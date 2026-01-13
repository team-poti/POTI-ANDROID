package com.poti.android.presentation.auth.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState

data class LoginState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
) : UiState

sealed interface LoginIntent : UiIntent {
    data object OnKakaoLoginClick : LoginIntent
}

sealed interface LoginEffect : UiEffect {
    data object NavigateToOnboarding : LoginEffect

    data object NavigateToHome : LoginEffect
}
