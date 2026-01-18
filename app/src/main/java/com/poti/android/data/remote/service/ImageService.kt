package com.poti.android.data.remote.service

import com.poti.android.data.remote.dto.base.ApiResponse
import com.poti.android.data.remote.dto.response.PresignedUrlListResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageService {
    @GET("api/v1/images/presigned-url")
    suspend fun getPresignedUrl(
        @Query("type") type: String,
        @Query("count") count: Int,
        @Query("extension") extension: String,
    ): ApiResponse<PresignedUrlListResponseDto>
}
