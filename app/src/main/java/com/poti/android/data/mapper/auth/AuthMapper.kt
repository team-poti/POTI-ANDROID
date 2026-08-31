package com.poti.android.data.mapper.auth

import com.poti.android.data.remote.dto.response.auth.LoginResponseDto
import com.poti.android.data.remote.dto.response.auth.WithdrawalReasonResponseDto
import com.poti.android.domain.model.auth.UserAuth
import com.poti.android.domain.model.auth.WithdrawalReason

fun LoginResponseDto.toDomain(): UserAuth = UserAuth(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken,
    isNewUser = this.isNewUser,
    userId = this.userId,
)

fun WithdrawalReasonResponseDto.toDomain(): WithdrawalReason = WithdrawalReason(
    code = code,
    label = label,
)
