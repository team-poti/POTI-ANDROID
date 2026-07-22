package com.poti.android.core.auth

import android.content.Context

interface SocialLoginProvider {
    suspend fun login(context: Context): SocialLoginResult
}
