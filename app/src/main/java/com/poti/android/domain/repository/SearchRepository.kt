package com.poti.android.domain.repository

import com.poti.android.domain.model.search.PartySearchResult

interface SearchRepository {
    suspend fun searchParties(
        keyword: String,
        page: Int,
        size: Int,
        sort: String?,
    ): Result<PartySearchResult>
}
