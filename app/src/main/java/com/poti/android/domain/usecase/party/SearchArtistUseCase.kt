package com.poti.android.domain.usecase.party

import com.poti.android.domain.model.artist.ArtistSearchResult
import com.poti.android.domain.repository.PartyRepository
import javax.inject.Inject

class SearchArtistUseCase @Inject constructor(
    private val partyRepository: PartyRepository,
) {
    suspend operator fun invoke(
        keyword: String,
    ): Result<List<ArtistSearchResult>> = partyRepository.searchArtist(keyword)
}
