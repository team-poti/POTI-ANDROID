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
            is LoginIntent.OnKakaoLoginClick -> loginKakao(intent.context)
            LoginIntent.OnGoogleLoginClick -> sendEffect(LoginEffect.NavigateToHome)
        }
    }

    fun loginKakao(context: Context) {
        Timber.d("카카오 로그인 실행")
        updateState { copy(loginState = ApiState.Loading) }

        kakaoLoginManager.login(context) { result ->
            result.onSuccess { token ->
                Timber.d("로그인 성공")
                requestServerLogin(token.accessToken)
            }
            result.onFailure { error ->
                Timber.e(error, "로그인 실패 원인: ${error.message}")
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
                    Timber.e(error, "서버 로그인 실패")
                    updateState { copy(loginState = ApiState.Failure(error.message ?: "Server Error")) }
                }
        }
    }
}
