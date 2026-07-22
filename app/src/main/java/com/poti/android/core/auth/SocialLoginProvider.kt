package com.poti.android.core.auth

import android.content.Context

interface SocialLoginProvider {
    fun login(
        context: Context,
        onResult: (SocialLoginResult) -> Unit,
    )
}
