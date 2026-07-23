package com.poti.android.data.auth

import android.content.Context

interface KakaoAuthClient {
    fun isKakaoTalkLoginAvailable(context: Context): Boolean

    fun loginWithKakaoTalk(
        context: Context,
        onResult: (accessToken: String?, error: Throwable?) -> Unit,
    )

    fun loginWithKakaoAccount(
        context: Context,
        onResult: (accessToken: String?, error: Throwable?) -> Unit,
    )

    fun isCancelled(error: Throwable?): Boolean
}
