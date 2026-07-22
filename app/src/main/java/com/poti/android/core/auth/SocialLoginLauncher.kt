package com.poti.android.core.auth

import android.content.Context
import com.poti.android.domain.model.auth.SocialType

interface SocialLoginLauncher {
    suspend fun login(
        context: Context,
        socialType: SocialType,
    ): SocialLoginResult
}
