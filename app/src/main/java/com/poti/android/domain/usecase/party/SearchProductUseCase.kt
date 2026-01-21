package com.poti.android.domain.usecase.party

import com.poti.android.domain.repository.PartyRepository
import javax.inject.Inject

class SearchProductUseCase @Inject constructor(
    private val partyRepository: PartyRepository,
) {
    suspend operator fun invoke(
        artistId: Long,
        keyword: String,
    ): Result<List<String>> = partyRepository.searchProductTitle(artistId, keyword)
}
