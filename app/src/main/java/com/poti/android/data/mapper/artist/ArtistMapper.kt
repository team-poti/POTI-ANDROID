package com.poti.android.data.mapper.artist

import com.poti.android.data.remote.dto.response.artist.ArtistListResponseDto
import com.poti.android.domain.model.artist.Artist

fun ArtistListResponseDto.toDomain(): List<Artist> = artists.map {
    Artist(
        artistId = it.artistId,
        name = it.name,
        logoImageUrl = it.logoImageUrl ?: "",
    )
}
