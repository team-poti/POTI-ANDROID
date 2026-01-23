package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.auth.LoginRequestDto
import com.poti.android.data.remote.dto.request.auth.ReissueRequestDto
import com.poti.android.data.remote.dto.response.auth.LoginResponseDto
import com.poti.android.data.remote.dto.response.auth.ReissueResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface AuthService {
    @POST("/api/v1/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequestDto,
    ): BaseResponse<LoginResponseDto>

    @POST("/api/v1/auth/reissue")
    fun reissue(
        @Body reissueRequest: ReissueRequestDto,
    ): Call<BaseResponse<ReissueResponseDto>>

    @DELETE("/api/v1/auth/withdrawal")
    suspend fun withdrawal(): BaseResponse<Unit>
}
