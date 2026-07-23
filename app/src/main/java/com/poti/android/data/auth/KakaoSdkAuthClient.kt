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
        onResult: (accessToken: String?, error: Throwable?) -> Unit,
    ) {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            onResult(token?.accessToken, error)
        }
    }

    override fun loginWithKakaoAccount(
        context: Context,
        onResult: (accessToken: String?, error: Throwable?) -> Unit,
    ) {
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            onResult(token?.accessToken, error)
        }
    }

    override fun isCancelled(error: Throwable?): Boolean =
        error is ClientError && error.reason == ClientErrorCause.Cancelled
}
