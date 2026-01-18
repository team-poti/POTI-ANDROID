package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.local.datasource.PreferenceDataSource
import com.poti.android.data.mapper.auth.toDomain
import com.poti.android.data.remote.datasource.AuthRemoteDataSource
import com.poti.android.data.remote.dto.request.auth.LoginRequestDto
import com.poti.android.domain.model.auth.UserAuth
import com.poti.android.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val preferenceDataSource: PreferenceDataSource,
) : AuthRepository {
    override suspend fun login(
        socialType: String,
        token: String,
    ): Result<UserAuth> = httpResponseHandler.safeApiCall {
        val requestDto = LoginRequestDto(
            socialType = socialType,
            token = token,
        )
        authRemoteDataSource.login(loginRequest = requestDto)
            .handleApiResponse()
            .getOrThrow()
            .apply { preferenceDataSource.saveTokens(accessToken, refreshToken) }
            .toDomain()
    }
}
