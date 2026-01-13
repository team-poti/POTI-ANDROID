package com.poti.android.presentation.auth

import android.content.Context
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.presentation.auth.model.LoginEffect
import com.poti.android.presentation.auth.model.LoginIntent
import com.poti.android.presentation.auth.model.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoLoginManager: KakaoLoginManager,
) : BaseViewModel<LoginState, LoginIntent, LoginEffect>(LoginState()) {
    override fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.OnKakaoLoginClick -> {}
        }
    }

    fun loginKakao(context: Context) {
        kakaoLoginManager.login(context) { result ->
            result.onSuccess { token ->
                requestServerLogin(token.accessToken)
            }
            result.onFailure { error ->
                updateState {
                    copy(loginState = ApiState.Failure(error.message ?: "카카오 로그인 실패"))
                }
            }
        }
    }

    private fun requestServerLogin(kakaoToken: String) {
        launchScope {
            // TODO: [지현] Repository를 통해 서버에 요청

            // TODO: [지현] 서버 응답 결과
            val isRegistered = false

            updateState { copy(loginState = ApiState.Success(Unit)) }

            if (isRegistered) {
                Timber.i("기존 회원입니다. 홈으로 이동합니다.")
                sendEffect(LoginEffect.NavigateToHome)
            } else {
                Timber.i("신규 회원입니다. 온보딩으로 이동합니다.")
                sendEffect(LoginEffect.NavigateToOnboarding)
            }
        }
    }
}
