package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.image.PresignedUrlListResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageService {
    @GET("api/v1/images/presigned-url")
    suspend fun getPresignedUrl(
        @Query("type") type: String,
        @Query("count") count: Int,
        @Query("extension") extension: String,
    ): BaseResponse<PresignedUrlListResponseDto>
}
