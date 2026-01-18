package com.poti.android.data.remote.datasource

import com.poti.android.data.remote.dto.base.ApiResponse
import com.poti.android.data.remote.dto.request.CreatePostRequestDto
import com.poti.android.data.remote.dto.response.CreatePostResponseDto
import com.poti.android.data.remote.dto.response.ProductSearchResponseDto
import com.poti.android.data.remote.service.PostService
import jakarta.inject.Inject

class PostRemoteDataSource @Inject constructor(
    private val postService: PostService,
) {
    suspend fun searchProductTitle(artistId: Long, keyword: String): ApiResponse<ProductSearchResponseDto> =
        postService.searchProductTitle(artistId, keyword)

    // TODO: [도연] 서버 수정 완료 시 업데이트
    suspend fun searchArtist() = postService.searchArtist()

    suspend fun createPost(body: CreatePostRequestDto): ApiResponse<CreatePostResponseDto> =
        postService.createPost(body)
}
