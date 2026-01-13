package com.poti.android.data.remote.datasource

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.LoginRequestDto
import com.poti.android.data.remote.dto.response.LoginResponseDto
import com.poti.android.data.remote.service.AuthService
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val authService: AuthService,
) {
    suspend fun login(
        socialType: String,
        token: String,
    ): BaseResponse<LoginResponseDto> =
        authService.login(loginRequest = LoginRequestDto(socialType, token))
}
