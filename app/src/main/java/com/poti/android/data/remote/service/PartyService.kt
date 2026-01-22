package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.party.CreatePartyRequestDto
import com.poti.android.data.remote.dto.response.artist.ArtistSearchListResponseDto
import com.poti.android.data.remote.dto.response.history.GroupBuyPostSaleDto
import com.poti.android.data.remote.dto.response.party.CreatePartyResponseDto
import com.poti.android.data.remote.dto.response.party.ProductSearchResponseDto
import com.poti.android.data.remote.dto.response.party.ShippingOptionResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PartyService {
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
    suspend fun createParty(
        @Body body: CreatePartyRequestDto,
    ): BaseResponse<CreatePartyResponseDto>

    @GET("/api/v1/shippings")
    suspend fun getShippingOptions(): BaseResponse<List<ShippingOptionResponseDto>>

    @GET("/api/v1/posts/sale/{postId}")
    suspend fun getPostSale(
        @Path("postId") postId: Long,
    ): BaseResponse<GroupBuyPostSaleDto>
}
