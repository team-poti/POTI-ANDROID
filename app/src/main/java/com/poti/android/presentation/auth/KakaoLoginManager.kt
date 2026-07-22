package com.poti.android.presentation.auth

import android.content.Context
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

sealed interface KakaoLoginResult {
    data class Success(val accessToken: String) : KakaoLoginResult

    data object Cancelled : KakaoLoginResult

    data class Failure(val cause: Throwable) : KakaoLoginResult
}

class KakaoLoginManager @Inject constructor() {
    fun login(
        context: Context,
        onResult: (KakaoLoginResult) -> Unit,
    ) {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                when {
                    token != null -> onResult(KakaoLoginResult.Success(token.accessToken))
                    error.isCancelled() -> onResult(KakaoLoginResult.Cancelled)
                    error != null -> loginWithKakaoAccount(context, onResult)
                    else -> onResult(
                        KakaoLoginResult.Failure(
                            IllegalStateException("Kakao login returned neither token nor error"),
                        ),
                    )
                }
            }
        } else {
            loginWithKakaoAccount(context, onResult)
        }
    }

    private fun loginWithKakaoAccount(
        context: Context,
        onResult: (KakaoLoginResult) -> Unit,
    ) {
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            when {
                token != null -> onResult(KakaoLoginResult.Success(token.accessToken))
                error.isCancelled() -> onResult(KakaoLoginResult.Cancelled)
                error != null -> onResult(KakaoLoginResult.Failure(error))
                else -> onResult(
                    KakaoLoginResult.Failure(
                        IllegalStateException("Kakao account login returned neither token nor error"),
                    ),
                )
            }
        }
    }

    private fun Throwable?.isCancelled(): Boolean =
        this is ClientError && reason == ClientErrorCause.Cancelled
}

@EntryPoint
@InstallIn(ActivityComponent::class)
interface KakaoLoginEntryPoint {
    fun kakaoLoginManager(): KakaoLoginManager
}
