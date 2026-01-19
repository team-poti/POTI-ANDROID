package com.poti.android.data.remote.datasource

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.post.CreatePostRequestDto
import com.poti.android.data.remote.dto.response.artist.ArtistSearchListResponseDto
import com.poti.android.data.remote.dto.response.post.CreatePostResponseDto
import com.poti.android.data.remote.dto.response.post.ProductSearchResponseDto
import com.poti.android.data.remote.dto.response.post.ShippingOptionResponseDto
import com.poti.android.data.remote.service.PostService
import jakarta.inject.Inject

class PostRemoteDataSource @Inject constructor(
    private val postService: PostService,
) {
    suspend fun searchProductTitle(artistId: Long, keyword: String): BaseResponse<ProductSearchResponseDto> =
        postService.searchProductTitle(artistId, keyword)

    suspend fun searchArtist(keyword: String): BaseResponse<ArtistSearchListResponseDto> =
        postService.searchArtist(keyword)

    suspend fun createPost(body: CreatePostRequestDto): BaseResponse<CreatePostResponseDto> =
        postService.createPost(body)

    suspend fun getShippingOptions(): BaseResponse<List<ShippingOptionResponseDto>> =
        postService.getShippingOptions()
}
