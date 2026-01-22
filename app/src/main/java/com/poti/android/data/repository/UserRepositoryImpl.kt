package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.user.toDomain
import com.poti.android.data.remote.datasource.UserRemoteDataSource
import com.poti.android.data.remote.dto.request.user.NicknameDuplicateRequestDto
import com.poti.android.data.remote.dto.request.user.OnboardingRequestDto
import com.poti.android.domain.model.user.UserMyPage
import com.poti.android.domain.model.user.UserProfile
import com.poti.android.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val userRemoteDataSource: UserRemoteDataSource,
) : UserRepository {
    override suspend fun patchOnboarding(
        nickname: String,
        favoriteArtistId: Long?,
    ): Result<Unit> = httpResponseHandler.safeApiCall {
        val requestDto = OnboardingRequestDto(
            nickname = nickname,
            favoriteArtistId = favoriteArtistId,
        )
        userRemoteDataSource.patchOnboarding(onboardingRequest = requestDto)
            .handleApiResponse()
            .getOrThrow()
    }

    override suspend fun postNicknameDuplicate(nickname: String): Result<Boolean> =
        httpResponseHandler.safeApiCall {
            val requestDto = NicknameDuplicateRequestDto(
                nickname = nickname,
            )
            userRemoteDataSource
                .postNicknameDuplicate(nicknameDuplicateRequest = requestDto)
                .handleApiResponse()
                .getOrThrow()
                .isDuplicated
        }

    override suspend fun getUserMyPage(): Result<UserMyPage> = httpResponseHandler.safeApiCall {
        userRemoteDataSource.getUserMyPage()
            .handleApiResponse()
            .getOrThrow()
            .toDomain()
    }

    override suspend fun getUserProfile(userId: Long): Result<UserProfile> = httpResponseHandler.safeApiCall {
        userRemoteDataSource.getUserProfile(userId)
            .handleApiResponse()
            .getOrThrow()
            .toDomain()
    }
}
