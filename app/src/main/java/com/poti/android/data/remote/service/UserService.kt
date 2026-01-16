package com.poti.android.data.remote.service

import com.poti.android.core.network.model.AuthType
import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.user.NicknameDuplicateRequestDto
import com.poti.android.data.remote.dto.request.user.OnboardingRequestDto
import com.poti.android.data.remote.dto.response.user.NicknameDuplicateResponseDto
import com.poti.android.data.remote.dto.response.user.OnboardingResponseDto
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserService {
    @Headers("Auth-Type: ${AuthType.BEARER}")
    @PATCH("/api/v1/users/onboarding")
    suspend fun patchOnboarding(
        @Body onboardingRequest: OnboardingRequestDto,
    ): BaseResponse<OnboardingResponseDto>

    @Headers("Auth-Type: ${AuthType.BEARER}")
    @POST("/api/v1/users/nickname/duplicate")
    suspend fun postNicknameDuplicate(
        @Body nicknameDuplicateRequest: NicknameDuplicateRequestDto,
    ): BaseResponse<NicknameDuplicateResponseDto>
}
