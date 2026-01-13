package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.toDomain
import com.poti.android.data.remote.datasource.AuthRemoteDataSource
import com.poti.android.domain.model.UserAuth
import com.poti.android.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val authRemoteDataSource: AuthRemoteDataSource,
) : AuthRepository {
    override suspend fun login(
        socialType: String,
        token: String,
    ): Result<UserAuth> = httpResponseHandler.safeApiCall {
        authRemoteDataSource.login(socialType = socialType, token = token)
            .handleApiResponse()
            .getOrThrow()
            .toDomain()
    }
}
