package com.poti.android.domain.repository

import com.poti.android.domain.model.artist.Artist

interface ArtistRepository {
    suspend fun getArtists(): Result<List<Artist>>
}
