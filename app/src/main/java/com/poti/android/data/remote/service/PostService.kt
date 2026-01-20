package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.post.CreatePostRequestDto
import com.poti.android.data.remote.dto.response.artist.ArtistSearchListResponseDto
import com.poti.android.data.remote.dto.response.post.CreatePostResponseDto
import com.poti.android.data.remote.dto.response.post.ProductSearchResponseDto
import com.poti.android.data.remote.dto.response.post.ShippingOptionResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PostService {
    @GET("/api/v1/posts/titles")
    suspend fun searchProductTitle(
        @Query("artistId") artistId: Long,
        @Query("keyword") keyword: String,
    ): BaseResponse<ProductSearchResponseDto>

    @GET("/api/v1/posts/artists")
    suspend fun searchArtist(
        @Query("keyword") keyword: String,
    ): BaseResponse<ArtistSearchListResponseDto>

    @POST("/api/v1/posts")
    suspend fun createPost(
        @Body body: CreatePostRequestDto,
    ): BaseResponse<CreatePostResponseDto>

    @GET("/api/v1/shippings")
    suspend fun getShippingOptions(): BaseResponse<List<ShippingOptionResponseDto>>
}
