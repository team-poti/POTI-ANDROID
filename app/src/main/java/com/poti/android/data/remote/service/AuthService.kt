package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.LoginRequestDto
import com.poti.android.data.remote.dto.response.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/api/v1/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequestDto,
    ): BaseResponse<LoginResponseDto>
}
