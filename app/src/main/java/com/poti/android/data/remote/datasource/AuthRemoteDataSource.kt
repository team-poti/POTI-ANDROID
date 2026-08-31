package com.poti.android.data.remote.datasource

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.di.ReissueClient
import com.poti.android.data.remote.dto.request.auth.LoginRequestDto
import com.poti.android.data.remote.dto.request.auth.ReissueRequestDto
import com.poti.android.data.remote.dto.request.auth.WithdrawalRequestDto
import com.poti.android.data.remote.dto.response.auth.LoginResponseDto
import com.poti.android.data.remote.dto.response.auth.ReissueResponseDto
import com.poti.android.data.remote.dto.response.auth.WithdrawalReasonResponseDto
import com.poti.android.data.remote.service.AuthService
import retrofit2.Call
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val authService: AuthService,
    @param:ReissueClient private val reissueAuthService: AuthService,
) {
    suspend fun login(loginRequest: LoginRequestDto): BaseResponse<LoginResponseDto> =
        authService.login(loginRequest = loginRequest)

    fun reissue(reissueRequest: ReissueRequestDto): Call<BaseResponse<ReissueResponseDto>> =
        reissueAuthService.reissue(reissueRequest = reissueRequest)

    suspend fun getWithdrawalReasons(): BaseResponse<List<WithdrawalReasonResponseDto>> =
        authService.getWithdrawalReasons()

    suspend fun withdrawal(request: WithdrawalRequestDto): BaseResponse<Unit> =
        authService.withdrawal(request = request)
}
