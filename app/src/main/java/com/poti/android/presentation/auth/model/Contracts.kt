package com.poti.android.presentation.auth.model

import com.poti.android.core.auth.SocialLoginResult
import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.domain.model.auth.SocialType

data class LoginState(
    val phase: LoginPhase = LoginPhase.IDLE,
) : UiState

sealed interface LoginIntent : UiIntent {
    data class OnSocialLoginClick(val socialType: SocialType) : LoginIntent

    data class OnSocialLoginResult(
        val socialType: SocialType,
        val result: SocialLoginResult,
    ) : LoginIntent

    data object OnSocialLoginAborted : LoginIntent

    data object OnBrowseAsGuestClick : LoginIntent
}

sealed interface LoginEffect : UiEffect {
    data object NavigateToOnboarding : LoginEffect

    data object NavigateToHome : LoginEffect

    data class LaunchSocialLogin(val socialType: SocialType) : LoginEffect
}
