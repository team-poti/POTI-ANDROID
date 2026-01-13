package com.poti.android.presentation.auth

import android.content.Context
import com.poti.android.core.base.BaseViewModel
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
            updateState { copy(isLoading = false) }
            result.onSuccess { token ->
                checkUserRegistration(token.accessToken)
            }
            result.onFailure { error ->
                Timber.e(error)
            }
        }
    }

    private fun checkUserRegistration(kakaoAccessToken: String) = launchScope {
        val isRegistered = false
        updateState { copy(isLoading = false, isLoggedIn = true) }

        if (isRegistered) {
            Timber.i("기존 회원입니다. 홈으로 이동합니다.")
            sendEffect(LoginEffect.NavigateToHome)
        } else {
            Timber.i("신규 회원입니다. 온보딩으로 이동합니다.")
            sendEffect(LoginEffect.NavigateToOnboarding)
        }
    }
}
