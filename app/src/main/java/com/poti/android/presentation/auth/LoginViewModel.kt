package com.poti.android.presentation.auth

import android.content.Context
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.repository.AuthRepository
import com.poti.android.presentation.auth.model.LoginEffect
import com.poti.android.presentation.auth.model.LoginIntent
import com.poti.android.presentation.auth.model.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoLoginManager: KakaoLoginManager,
    private val authRepository: AuthRepository,
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
            authRepository.login(socialType = "KAKAO", token = kakaoToken)
                .onSuccess { response ->
                    val loginData = response

                    // TODO: [지현] 토큰 저장

                    updateState { copy(loginState = ApiState.Success(Unit)) }

                    if (loginData.isNewUser) {
                        Timber.i("신규 회원입니다. 온보딩으로 이동합니다.")
                        sendEffect(LoginEffect.NavigateToOnboarding)
                    } else {
                        Timber.i("기존 회원입니다. 홈으로 이동합니다.")
                        sendEffect(LoginEffect.NavigateToHome)
                    }
                }
                .onFailure { error ->
                    updateState { copy(loginState = ApiState.Failure(error.message ?: "Server Error")) }
                }
        }
    }
}
