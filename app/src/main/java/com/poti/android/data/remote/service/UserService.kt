package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.user.NicknameDuplicateRequestDto
import com.poti.android.data.remote.dto.request.user.OnboardingRequestDto
import com.poti.android.data.remote.dto.response.user.NicknameDuplicateResponseDto
import com.poti.android.data.remote.dto.response.user.OnboardingResponseDto
import com.poti.android.data.remote.dto.response.user.ProfileResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @PATCH("/api/v1/users/onboarding")
    suspend fun patchOnboarding(
        @Body onboardingRequest: OnboardingRequestDto,
    ): BaseResponse<OnboardingResponseDto>

    @POST("/api/v1/users/nickname/duplicate")
    suspend fun postNicknameDuplicate(
        @Body nicknameDuplicateRequest: NicknameDuplicateRequestDto,
    ): BaseResponse<NicknameDuplicateResponseDto>

    @GET("/api/v1/users/{userId}/profile")
    suspend fun getUserProfile(
        @Path("userId") userId: Long,
    ): BaseResponse<ProfileResponseDto>
}
