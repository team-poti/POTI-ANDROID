package com.poti.android.data.remote.datasource

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.artist.ArtistListResponseDto
import com.poti.android.data.remote.service.ArtistService
import javax.inject.Inject

class ArtistRemoteDataSource @Inject constructor(
    private val artistService: ArtistService,
) {
    suspend fun getArtists(): BaseResponse<ArtistListResponseDto> =
        artistService.getArtists()
}
