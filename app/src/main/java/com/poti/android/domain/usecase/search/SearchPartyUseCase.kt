package com.poti.android.domain.usecase.search

import com.poti.android.domain.model.search.PartySearchResult
import com.poti.android.domain.repository.SearchRepository
import javax.inject.Inject

class SearchPartyUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
) {
    suspend operator fun invoke(
        keyword: String,
        page: Int,
        size: Int,
    ): Result<PartySearchResult> = searchRepository.searchParties(keyword, page, size)
}
