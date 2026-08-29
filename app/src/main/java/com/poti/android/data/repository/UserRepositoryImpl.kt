package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.model.handleNullableApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.user.toDomain
import com.poti.android.data.mock.UiMockData
import com.poti.android.data.mock.executeWithUiMock
import com.poti.android.data.remote.datasource.UserRemoteDataSource
import com.poti.android.data.remote.dto.request.user.EditProfileRequestDto
import com.poti.android.data.remote.dto.request.user.FavoriteArtistRequestDto
import com.poti.android.data.remote.dto.request.user.NicknameDuplicateRequestDto
import com.poti.android.data.remote.dto.request.user.OnboardingRequestDto
import com.poti.android.domain.model.delivery.DeliveryInfo
import com.poti.android.domain.model.user.UserAccount
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
    ): Result<Unit> = executeWithUiMock(
        mock = { Unit },
        real = {
            httpResponseHandler.safeApiCall {
                val requestDto = OnboardingRequestDto(
                    nickname = nickname,
                    favoriteArtistId = favoriteArtistId,
                )
                userRemoteDataSource.patchOnboarding(onboardingRequest = requestDto)
                    .handleApiResponse()
                    .getOrThrow()
                Unit
            }
        },
    )

    override suspend fun patchFavoriteArtist(artistId: Long): Result<Unit> = executeWithUiMock(
        mock = { Unit },
        real = {
            httpResponseHandler.safeApiCall {
                val requestDto = FavoriteArtistRequestDto(artistId = artistId)
                userRemoteDataSource.patchFavoriteArtist(favoriteArtistRequest = requestDto)
                    .handleNullableApiResponse()
                    .getOrThrow()
                Unit
            }
        },
    )

    override suspend fun postNicknameDuplicate(nickname: String): Result<Boolean> =
        executeWithUiMock(
            mock = { false },
            real = {
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
            },
        )

    override suspend fun getUserMyPage(): Result<UserMyPage> = executeWithUiMock(
        mock = { UiMockData.userMyPage },
        real = {
            httpResponseHandler.safeApiCall {
                userRemoteDataSource.getUserMyPage()
                    .handleApiResponse()
                    .getOrThrow()
                    .toDomain()
            }
        },
    )

    override suspend fun getUserProfile(userId: Long): Result<UserProfile> = executeWithUiMock(
        mock = { UiMockData.userProfile.copy(userId = userId) },
        real = {
            httpResponseHandler.safeApiCall {
                userRemoteDataSource.getUserProfile(userId)
                    .handleApiResponse()
                    .getOrThrow()
                    .toDomain()
            }
        },
    )

    override suspend fun getUserAccount(): Result<UserAccount> = executeWithUiMock(
        mock = { UiMockData.userAccount },
        real = {
            httpResponseHandler.safeApiCall {
                userRemoteDataSource.getUserAccount()
                    .handleApiResponse()
                    .getOrThrow()
                    .toDomain()
            }
        },
    )

    override suspend fun getMyAddress(): Result<DeliveryInfo?> = executeWithUiMock(
        mock = { UiMockData.myAddress },
        real = {
            httpResponseHandler.safeApiCall {
                userRemoteDataSource.getMyAddress()
                    .handleNullableApiResponse()
                    .getOrThrow()
                    ?.toDomain()
            }
        },
    )

    override suspend fun patchProfile(
        nickname: String,
        profileImageUrl: String,
    ): Result<Unit> = executeWithUiMock(
        mock = { Unit },
        real = {
            httpResponseHandler.safeApiCall {
                val requestDto = EditProfileRequestDto(
                    nickname = nickname,
                    profileImageUrl = profileImageUrl,
                )
                userRemoteDataSource.patchProfile(editProfileRequest = requestDto)
                    .handleNullableApiResponse()
                    .getOrThrow()
                Unit
            }
        },
    )
}
