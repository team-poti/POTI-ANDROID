package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.image.toDomain
import com.poti.android.data.remote.datasource.ImageRemoteDataSource
import com.poti.android.domain.model.image.PresignedUploadInfo
import com.poti.android.domain.repository.ImageRepository
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val imageRemoteDataSource: ImageRemoteDataSource,
) : ImageRepository {
    override suspend fun getPresignedUrls(type: String, count: Int, extension: String): Result<List<PresignedUploadInfo>> = httpResponseHandler.safeApiCall {
        imageRemoteDataSource.getPresignedUrls(type, count, extension)
            .handleApiResponse()
            .getOrThrow()
            .toDomain()
    }
}
