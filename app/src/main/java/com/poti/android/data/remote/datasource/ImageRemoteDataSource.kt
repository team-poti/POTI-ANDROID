package com.poti.android.data.remote.datasource

import com.poti.android.data.remote.dto.base.ApiResponse
import com.poti.android.data.remote.dto.response.PresignedUrlListResponseDto
import com.poti.android.data.remote.service.ImageService
import javax.inject.Inject

class ImageRemoteDataSource @Inject constructor(
    private val imageService: ImageService,
) {
    suspend fun getPresignedUrl(type: String, count: Int, extension: String): ApiResponse<PresignedUrlListResponseDto> =
        imageService.getPresignedUrl(type, count, extension)
}
