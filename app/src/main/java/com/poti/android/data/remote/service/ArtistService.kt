package com.poti.android.data.remote.service

import com.poti.android.data.remote.dto.base.ApiResponse
import com.poti.android.data.remote.dto.response.MemberListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ArtistService {
    @GET("api/v1/artists/{artistId}/members")
    suspend fun getMemberList(
        @Path("artistId") artistId: Long,
    ): ApiResponse<MemberListResponseDto>
}
