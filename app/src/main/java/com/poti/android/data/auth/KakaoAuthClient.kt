package com.poti.android.data.auth

import android.content.Context

interface KakaoAuthClient {
    fun isKakaoTalkLoginAvailable(context: Context): Boolean

    fun loginWithKakaoTalk(
        context: Context,
        onResult: (KakaoAuthResult) -> Unit,
    )

    fun loginWithKakaoAccount(
        context: Context,
        onResult: (KakaoAuthResult) -> Unit,
    )
}
