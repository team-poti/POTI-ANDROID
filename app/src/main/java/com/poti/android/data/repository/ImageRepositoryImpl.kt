package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.image.toDomain
import com.poti.android.data.mock.UiMockData
import com.poti.android.data.mock.executeWithUiMock
import com.poti.android.data.remote.datasource.ImageRemoteDataSource
import com.poti.android.domain.model.image.PresignedUploadInfo
import com.poti.android.domain.repository.ImageRepository
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val imageRemoteDataSource: ImageRemoteDataSource,
) : ImageRepository {
    override suspend fun getPresignedUrls(
        type: String,
        extensions: List<String>,
    ): Result<List<PresignedUploadInfo>> = executeWithUiMock(
        mock = { UiMockData.presignedUploadInfos(extensions) },
        real = {
            httpResponseHandler.safeApiCall {
                imageRemoteDataSource.getPresignedUrls(type, extensions)
                    .handleApiResponse()
                    .getOrThrow()
                    .toDomain()
            }
        },
    )
}
