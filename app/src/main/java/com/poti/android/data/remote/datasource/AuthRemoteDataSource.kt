package com.poti.android.data.remote.datasource

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.auth.LoginRequestDto
import com.poti.android.data.remote.dto.request.auth.ReissueRequestDto
import com.poti.android.data.remote.dto.response.auth.LoginResponseDto
import com.poti.android.data.remote.dto.response.auth.ReissueResponseDto
import com.poti.android.data.remote.service.AuthService
import retrofit2.Call
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val authService: AuthService,
) {
    suspend fun login(loginRequest: LoginRequestDto): BaseResponse<LoginResponseDto> =
        authService.login(loginRequest = loginRequest)

    fun reissue(reissueRequest: ReissueRequestDto): Call<BaseResponse<ReissueResponseDto>> =
        authService.reissue(reissueRequest = reissueRequest)
}
