package com.poti.android.presentation.auth

import com.poti.android.core.auth.SocialLoginResult
import com.poti.android.core.base.BaseViewModel
import com.poti.android.domain.model.auth.SocialType
import com.poti.android.domain.usecase.auth.ClearPendingReturnDeepLinkUseCase
import com.poti.android.domain.usecase.auth.EnterGuestModeUseCase
import com.poti.android.domain.usecase.auth.LoginUseCase
import com.poti.android.presentation.auth.model.LoginEffect
import com.poti.android.presentation.auth.model.LoginIntent
import com.poti.android.presentation.auth.model.LoginPhase
import com.poti.android.presentation.auth.model.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val enterGuestModeUseCase: EnterGuestModeUseCase,
    private val clearPendingReturnDeepLinkUseCase: ClearPendingReturnDeepLinkUseCase,
) : BaseViewModel<LoginState, LoginIntent, LoginEffect>(LoginState()) {
    fun onLoginCancelled() {
        clearPendingReturnDeepLinkUseCase()
    }

    override fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.OnSocialLoginClick -> {
                if (tryStartLogin()) {
                    sendEffect(LoginEffect.LaunchSocialLogin(intent.socialType))
                }
            }
            is LoginIntent.OnSocialLoginResult -> handleSocialLoginResult(intent)
            LoginIntent.OnSocialLoginAborted -> finishLogin()
            LoginIntent.OnBrowseAsGuestClick -> enterGuestMode()
        }
    }

    private fun enterGuestMode() {
        if (!uiState.value.phase.canStartLogin) return

        updateState { copy(phase = LoginPhase.SUCCESS) }
        clearPendingReturnDeepLinkUseCase()
        enterGuestModeUseCase()
        sendEffect(LoginEffect.NavigateToHome)
    }

    private fun handleSocialLoginResult(intent: LoginIntent.OnSocialLoginResult) {
        if (uiState.value.phase != LoginPhase.SOCIAL_LOGIN) return

        when (val result = intent.result) {
            is SocialLoginResult.Success -> requestServerLogin(
                socialType = intent.socialType,
                token = result.token,
            )
            SocialLoginResult.Cancelled -> {
                finishLogin()
                Timber.d("소셜 로그인이 취소되었습니다: ${intent.socialType}")
            }
            is SocialLoginResult.Failure -> handleSocialLoginFailure(
                socialType = intent.socialType,
                cause = result.cause,
            )
        }
    }

    private fun tryStartLogin(): Boolean {
        if (!uiState.value.phase.canStartLogin) return false

        updateState { copy(phase = LoginPhase.SOCIAL_LOGIN) }
        return true
    }

    private fun finishLogin() {
        updateState { copy(phase = LoginPhase.IDLE) }
    }

    private fun requestServerLogin(
        socialType: SocialType,
        token: String,
    ) {
        if (uiState.value.phase != LoginPhase.SOCIAL_LOGIN) return

        updateState { copy(phase = LoginPhase.SERVER_LOGIN) }

        launchScope(onError = ::handleServerLoginFailure) {
            loginUseCase(socialType = socialType, token = token)
                .onSuccess { response ->
                    finishLogin()
                    if (response.isNewUser) {
                        Timber.i("신규 회원입니다. 온보딩 상태: 미완료(false)로 저장 -> 온보딩으로 이동")
                        sendEffect(LoginEffect.NavigateToOnboarding)
                    } else {
                        Timber.i("기존 회원입니다. 온보딩 상태: 완료(true)로 저장 -> 홈으로 이동")
                        sendEffect(LoginEffect.NavigateToHome)
                    }
                }
                .onFailure { error ->
                    handleServerLoginFailure(error)
                }
        }
    }

    private fun handleSocialLoginFailure(
        socialType: SocialType,
        cause: Throwable,
    ) {
        Timber.e(cause, "소셜 로그인 실패: $socialType")
        finishLogin()
    }

    private fun handleServerLoginFailure(error: Throwable) {
        Timber.e(error, "서버 로그인 실패")
        finishLogin()
    }
}
