package com.poti.android.data.remote.service

import com.poti.android.data.remote.dto.base.ApiResponse
import com.poti.android.data.remote.dto.response.MemberListResponseDto
import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.artist.ArtistListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ArtistService {
    @GET("api/v1/artists/{artistId}/members")
    suspend fun getMemberList(
        @Path("artistId") artistId: Long,
    ): ApiResponse<MemberListResponseDto>

    @GET("/api/v1/artists")
    suspend fun getArtists(): BaseResponse<ArtistListResponseDto>
}
