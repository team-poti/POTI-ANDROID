package com.poti.android.core.notification.repository

import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.core.notification.remote.datasource.FcmRemoteDataSource
import com.poti.android.core.notification.remote.dto.request.FcmTokenRequestDto
import com.poti.android.data.mock.executeWithUiMock
import javax.inject.Inject

class FcmRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val remoteDataSource: FcmRemoteDataSource,
) : FcmRepository {
    override suspend fun deleteFcmToken(token: String): Result<Unit> = executeWithUiMock(
        mock = { },
        real = {
            httpResponseHandler.safeApiCall {
                remoteDataSource.deleteFcmToken(token)
                Unit
            }
        }
    )

    override suspend fun saveFcmToken(token: String): Result<Unit> = executeWithUiMock(
        mock = { },
        real = {
            httpResponseHandler.safeApiCall {
                remoteDataSource.postFcmToken(
                    request = FcmTokenRequestDto(token)
                )
                Unit
            }
        }
    )
}
