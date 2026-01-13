package com.poti.android.data.mapper

import com.poti.android.data.remote.dto.response.LoginResponseDto
import com.poti.android.domain.model.UserAuth

fun LoginResponseDto.toDomain(): UserAuth = UserAuth(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken,
    isNewUser = this.isNewUser,
    userId = this.userId,
)
