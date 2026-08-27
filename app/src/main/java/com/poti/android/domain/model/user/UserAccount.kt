package com.poti.android.domain.model.user

import com.poti.android.domain.model.auth.SocialType

data class UserAccount(
    val nickname: String,
    val email: String,
    val socialType: SocialType,
)
