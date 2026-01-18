package com.poti.android.data.remote.service

import com.poti.android.data.remote.dto.base.ApiResponse
import com.poti.android.data.remote.dto.request.CreatePostRequestDto
import com.poti.android.data.remote.dto.response.CreatePostResponseDto
import com.poti.android.data.remote.dto.response.ProductSearchResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PostService {
    @GET("api/v1/posts/titles")
    suspend fun searchProductTitle(
        @Query("artistId") artistId : Long,
        @Query("keyword") keyword: String,
    ): ApiResponse<ProductSearchResponseDto>

    // TODO: [도연] 서버 수정 완료 시 업데이트
    @GET("api/v1/posts/artists")
    suspend fun searchArtist()

    @POST("api/v1/posts")
    suspend fun createPot(
        @Body body: CreatePostRequestDto
    ): ApiResponse<CreatePostResponseDto>
}
