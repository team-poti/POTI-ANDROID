package com.poti.android.data.remote.datasource

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.image.PresignedUrlListResponseDto
import com.poti.android.data.remote.service.ImageService
import javax.inject.Inject

class ImageRemoteDataSource @Inject constructor(
    private val imageService: ImageService,
) {
    suspend fun getPresignedUrls(
        type: String,
        count: Int,
        extension: String,
    ): BaseResponse<PresignedUrlListResponseDto> =
        imageService.getPresignedUrls(type, count, extension)
}
