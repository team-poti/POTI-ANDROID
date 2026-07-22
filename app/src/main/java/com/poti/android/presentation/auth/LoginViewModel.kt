package com.poti.android.presentation.auth

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
            is LoginIntent.OnKakaoLoginClick -> {
                if (tryStartLogin()) sendEffect(LoginEffect.LaunchKakaoLogin)
            }
            LoginIntent.OnGoogleLoginClick -> {
                if (tryStartLogin()) sendEffect(LoginEffect.NavigateToHome)
            }
            is LoginIntent.OnKakaoLoginSuccess -> requestServerLogin(intent.token)
            LoginIntent.OnKakaoLoginCancelled -> {
                finishLogin()
                Timber.d("카카오 로그인이 취소되었습니다.")
            }
            is LoginIntent.OnKakaoLoginFailure -> {
                isLoginInProgress.set(false)
                Timber.e("카카오 로그인 실패: ${intent.message}")
                updateState { copy(loginState = ApiState.Failure(intent.message)) }
            }
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

    private fun requestServerLogin(kakaoToken: String) {
        if (!isServerLoginRequested.compareAndSet(false, true)) return

        launchScope(onError = ::handleServerLoginFailure) {
            loginUseCase(socialType = SocialType.KAKAO, token = kakaoToken)
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

    private fun handleServerLoginFailure(error: Throwable) {
        isLoginInProgress.set(false)
        isServerLoginRequested.set(false)
        Timber.e(error, "서버 로그인 실패")
        updateState { copy(loginState = ApiState.Failure(error.message ?: "Server Error")) }
    }
}
