package com.poti.android.data.mapper.artist

import com.poti.android.data.remote.dto.response.artist.ArtistSearchListResponseDto
import com.poti.android.domain.model.artist.ArtistSearchResult

fun ArtistSearchListResponseDto.toDomain(): List<ArtistSearchResult> = artists.map {
    ArtistSearchResult(
        artistId = it.artistId,
        name = it.name
    )
}
