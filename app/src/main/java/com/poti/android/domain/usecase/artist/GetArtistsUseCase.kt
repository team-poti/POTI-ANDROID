package com.poti.android.domain.usecase.artist

import com.poti.android.domain.model.artist.Artist
import com.poti.android.domain.repository.ArtistRepository
import javax.inject.Inject

class GetArtistsUseCase @Inject constructor(
    private val artistRepository: ArtistRepository,
) {
    suspend operator fun invoke(): Result<List<Artist>> = artistRepository.getArtists()
}
