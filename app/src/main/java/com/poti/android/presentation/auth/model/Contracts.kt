package com.poti.android.presentation.auth.model

import android.content.Context
import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState

data class LoginState(
    val loginState: ApiState<Unit> = ApiState.Init,
) : UiState

sealed interface LoginIntent : UiIntent {
    data class OnKakaoLoginClick(val context: Context) : LoginIntent

    data object OnGoogleLoginClick : LoginIntent
}

sealed interface LoginEffect : UiEffect {
    data object NavigateToOnboarding : LoginEffect

    data object NavigateToHome : LoginEffect
}
