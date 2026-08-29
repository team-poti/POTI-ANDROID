package com.poti.android.data.mapper.user

import com.poti.android.data.remote.dto.response.user.AccountResponseDto
import com.poti.android.domain.model.auth.SocialType
import com.poti.android.domain.model.user.UserAccount

fun AccountResponseDto.toDomain(): UserAccount =
    UserAccount(
        nickname = nickname,
        email = email,
        socialType = SocialType.valueOf(socialType),
    )
