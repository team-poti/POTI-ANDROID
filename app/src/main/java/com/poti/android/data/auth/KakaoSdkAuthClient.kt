package com.poti.android.data.auth

import android.content.Context
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import javax.inject.Inject

class KakaoSdkAuthClient @Inject constructor() : KakaoAuthClient {
    override fun isKakaoTalkLoginAvailable(context: Context): Boolean =
        UserApiClient.instance.isKakaoTalkLoginAvailable(context)

    override fun loginWithKakaoTalk(
        context: Context,
        onResult: (KakaoAuthResult) -> Unit,
    ) {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            onResult(
                mapResult(
                    accessToken = token?.accessToken,
                    error = error,
                    invalidResponseMessage = "KakaoTalk login returned neither token nor error",
                ),
            )
        }
    }

    override fun loginWithKakaoAccount(
        context: Context,
        onResult: (KakaoAuthResult) -> Unit,
    ) {
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            onResult(
                mapResult(
                    accessToken = token?.accessToken,
                    error = error,
                    invalidResponseMessage = "Kakao account login returned neither token nor error",
                ),
            )
        }
    }

    private fun mapResult(
        accessToken: String?,
        error: Throwable?,
        invalidResponseMessage: String,
    ): KakaoAuthResult =
        when {
            error is ClientError && error.reason == ClientErrorCause.Cancelled -> {
                KakaoAuthResult.Cancelled
            }
            error != null -> KakaoAuthResult.Failure(error)
            !accessToken.isNullOrBlank() -> KakaoAuthResult.Success(accessToken)
            else -> KakaoAuthResult.InvalidResponse(
                IllegalStateException(invalidResponseMessage),
            )
        }
}
