package com.poti.android.core.fcm.repository

import com.poti.android.core.fcm.remote.datasource.FcmRemoteDataSource
import com.poti.android.core.fcm.remote.dto.request.FcmTokenRequestDto
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.local.datasource.AuthTokenStore
import com.poti.android.data.mock.executeWithUiMock
import javax.inject.Inject

class FcmRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val remoteDataSource: FcmRemoteDataSource,
    private val authTokenStore: AuthTokenStore,
) : FcmRepository {
    override suspend fun deleteFcmToken(token: String): Result<Unit> = executeWithUiMock(
        mock = { },
        real = {
            httpResponseHandler.safeApiCall {
                remoteDataSource.deleteFcmToken(token)
                Unit
            }
        },
    )

    override suspend fun saveFcmToken(token: String): Result<Unit> = executeWithUiMock(
        mock = { },
        real = {
            if (authTokenStore.cachedAccessToken == null) return@executeWithUiMock Result.success(Unit)

            httpResponseHandler.safeApiCall {
                remoteDataSource.postFcmToken(
                    request = FcmTokenRequestDto(token),
                )
                Unit
            }
        },
    )
}
