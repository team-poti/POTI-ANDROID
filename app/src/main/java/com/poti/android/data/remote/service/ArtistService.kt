package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.artist.ArtistListResponseDto
import retrofit2.http.GET

interface ArtistService {
    @GET("/api/v1/artists")
    suspend fun getArtists(): BaseResponse<ArtistListResponseDto>
}
