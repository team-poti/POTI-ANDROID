package com.poti.android.presentation.auth

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

class KakaoLoginManager @Inject constructor() {
    fun login(
        context: Context,
        onResult: (Result<OAuthToken>) -> Unit,
    ) {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    onResult(Result.failure(error))
                    return@loginWithKakaoTalk
                }
                when {
                    token != null -> onResult(Result.success(token))
                    error != null -> loginWithKakaoAccount(context, onResult)
                    else -> onResult(Result.failure(Exception("Unknown error: token and error are both null")))
                }
            }
        } else {
            loginWithKakaoAccount(context, onResult)
        }
    }

    private fun loginWithKakaoAccount(
        context: Context,
        onResult: (Result<OAuthToken>) -> Unit,
    ) {
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            if (token != null) {
                onResult(Result.success(token))
            } else {
                onResult(Result.failure(error ?: Exception("Unknown error")))
            }
        }
    }
}

@EntryPoint
@InstallIn(ActivityComponent::class)
interface KakaoLoginEntryPoint {
    fun kakaoLoginManager(): KakaoLoginManager
}
