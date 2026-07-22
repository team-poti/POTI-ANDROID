package com.poti.android.core.auth.kakao

import android.content.Context
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.poti.android.core.auth.SocialLoginProvider
import com.poti.android.core.auth.SocialLoginResult
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

class KakaoLoginProvider @Inject constructor() : SocialLoginProvider {
    override fun login(
        context: Context,
        onResult: (SocialLoginResult) -> Unit,
    ) {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                when {
                    token != null -> onResult(SocialLoginResult.Success(token.accessToken))
                    error.isCancelled() -> onResult(SocialLoginResult.Cancelled)
                    error != null -> loginWithKakaoAccount(context, onResult)
                    else -> onResult(
                        SocialLoginResult.Failure(
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
        onResult: (SocialLoginResult) -> Unit,
    ) {
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            when {
                token != null -> onResult(SocialLoginResult.Success(token.accessToken))
                error.isCancelled() -> onResult(SocialLoginResult.Cancelled)
                error != null -> onResult(SocialLoginResult.Failure(error))
                else -> onResult(
                    SocialLoginResult.Failure(
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
    fun kakaoLoginProvider(): KakaoLoginProvider
}
