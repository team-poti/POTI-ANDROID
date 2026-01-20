package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.artist.ArtistListResponseDto
import com.poti.android.data.remote.dto.response.artist.MemberListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ArtistService {
    @GET("/api/v1/artists/{artistId}/members")
    suspend fun getMemberList(
        @Path("artistId") artistId: Long,
    ): BaseResponse<MemberListResponseDto>

    @GET("/api/v1/artists")
    suspend fun getArtists(): BaseResponse<ArtistListResponseDto>
}
