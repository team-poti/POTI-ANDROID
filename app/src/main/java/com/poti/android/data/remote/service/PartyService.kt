package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.party.CreatePartyRequestDto
import com.poti.android.data.remote.dto.request.party.PartyJoinRequestDto
import com.poti.android.data.remote.dto.response.artist.ArtistSearchListResponseDto
import com.poti.android.data.remote.dto.response.history.GroupBuyPostParticipantDetailDto
import com.poti.android.data.remote.dto.response.history.RecruiterDetailDto
import com.poti.android.data.remote.dto.response.party.CreatePartyResponseDto
import com.poti.android.data.remote.dto.response.party.MyRecruitListDto
import com.poti.android.data.remote.dto.response.party.PartyDetailResponseDto
import com.poti.android.data.remote.dto.response.party.PartyJoinOptionsDto
import com.poti.android.data.remote.dto.response.party.PartyJoinResponseDto
import com.poti.android.data.remote.dto.response.party.ProductPartyListResponseDto
import com.poti.android.data.remote.dto.response.party.ProductSearchResponseDto
import com.poti.android.data.remote.dto.response.party.ShippingOptionResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
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
        @Body createPartyRequest: CreatePartyRequestDto,
    ): BaseResponse<CreatePartyResponseDto>

    @GET("/api/v1/shippings")
    suspend fun getShippingOptions(): BaseResponse<List<ShippingOptionResponseDto>>

    @GET("/api/v1/posts/{postId}")
    suspend fun getPartyDetail(
        @Path("postId") partyId: Long,
    ): BaseResponse<PartyDetailResponseDto>

    @GET("/api/v1/posts/{postId}/options")
    suspend fun getPartyJoinOptions(
        @Path("postId") partyId: Long,
    ): BaseResponse<PartyJoinOptionsDto>

    @POST("/api/v1/orders")
    suspend fun postPartyJoin(
        @Body partyJoinRequest: PartyJoinRequestDto,
    ): BaseResponse<PartyJoinResponseDto>

    @GET("/api/v1/posts/me")
    suspend fun getMyRecruitList(
        @Query("status") status: String,
    ): BaseResponse<MyRecruitListDto>

    @GET("/api/v1/posts/sale/{postId}")
    suspend fun getRecruitDetail(
        @Path("postId") postId: Long,
    ): BaseResponse<RecruiterDetailDto>

    @GET("/api/v1/posts/{postId}/participants")
    suspend fun getRecruitPostParticipant(
        @Path("postId") postId: Long,
    ): BaseResponse<GroupBuyPostParticipantDetailDto>

    @DELETE("/api/v1/posts/{postId}")
    suspend fun deleteParty(
        @Path("postId") postId: Long,
    ): BaseResponse<Unit>

    @GET("/api/v1/posts/pots")
    suspend fun getProductPartyList(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("title") title: String,
        @Query("artistId") artistId: Long,
        @Query("sort") sort: String,
        @Query("memberIds") memberIds: List<Long>?,
    ): BaseResponse<ProductPartyListResponseDto>
}
