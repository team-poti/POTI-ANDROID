package com.poti.android.presentation.auth

import com.poti.android.core.auth.SocialLoginResult
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.auth.SocialType
import com.poti.android.domain.usecase.auth.LoginUseCase
import com.poti.android.presentation.auth.model.LoginEffect
import com.poti.android.presentation.auth.model.LoginIntent
import com.poti.android.presentation.auth.model.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : BaseViewModel<LoginState, LoginIntent, LoginEffect>(LoginState()) {
    private val isLoginInProgress = AtomicBoolean(false)
    private val isServerLoginRequested = AtomicBoolean(false)

    override fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.OnSocialLoginClick -> {
                if (tryStartLogin()) {
                    sendEffect(LoginEffect.LaunchSocialLogin(intent.socialType))
                }
            }
            is LoginIntent.OnSocialLoginResult -> handleSocialLoginResult(intent)
            LoginIntent.OnSocialLoginAborted -> finishLogin()
        }
    }

    private fun handleSocialLoginResult(intent: LoginIntent.OnSocialLoginResult) {
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
        if (!isLoginInProgress.compareAndSet(false, true)) return false

        isServerLoginRequested.set(false)
        updateState { copy(loginState = ApiState.Loading) }
        return true
    }

    private fun finishLogin() {
        isLoginInProgress.set(false)
        isServerLoginRequested.set(false)
        updateState { copy(loginState = ApiState.Init) }
    }

    private fun requestServerLogin(
        socialType: SocialType,
        token: String,
    ) {
        if (!isServerLoginRequested.compareAndSet(false, true)) return

        launchScope(onError = ::handleServerLoginFailure) {
            loginUseCase(socialType = socialType, token = token)
                .onSuccess { response ->
                    if (response.isNewUser) {
                        Timber.i("신규 회원입니다. 온보딩 상태: 미완료(false)로 저장 -> 온보딩으로 이동")
                        updateState { copy(loginState = ApiState.Success(Unit)) }
                        sendEffect(LoginEffect.NavigateToOnboarding)
                    } else {
                        Timber.i("기존 회원입니다. 온보딩 상태: 완료(true)로 저장 -> 홈으로 이동")
                        updateState { copy(loginState = ApiState.Success(Unit)) }
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
        isLoginInProgress.set(false)
        isServerLoginRequested.set(false)
        Timber.e(cause, "소셜 로그인 실패: $socialType")
        updateState { copy(loginState = ApiState.Failure(cause.message ?: "Social login failed")) }
    }

    private fun handleServerLoginFailure(error: Throwable) {
        isLoginInProgress.set(false)
        isServerLoginRequested.set(false)
        Timber.e(error, "서버 로그인 실패")
        updateState { copy(loginState = ApiState.Failure(error.message ?: "Server Error")) }
    }
}
