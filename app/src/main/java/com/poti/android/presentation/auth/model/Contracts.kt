package com.poti.android.presentation.auth.model

import com.poti.android.core.auth.SocialLoginResult
import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.auth.SocialType

data class LoginState(
    val loginState: ApiState<Unit> = ApiState.Init,
) : UiState

sealed interface LoginIntent : UiIntent {
    data object OnKakaoLoginClick : LoginIntent

    data class OnSocialLoginResult(
        val socialType: SocialType,
        val result: SocialLoginResult,
    ) : LoginIntent

    data object OnGoogleLoginClick : LoginIntent
}

sealed interface LoginEffect : UiEffect {
    data object NavigateToOnboarding : LoginEffect

    data object NavigateToHome : LoginEffect

    data object LaunchKakaoLogin : LoginEffect
}
