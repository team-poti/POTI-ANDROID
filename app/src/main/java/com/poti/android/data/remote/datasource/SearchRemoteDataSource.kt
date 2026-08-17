package com.poti.android.data.remote.datasource

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.search.PartySearchResponseDto
import com.poti.android.data.remote.service.SearchService
import javax.inject.Inject

class SearchRemoteDataSource @Inject constructor(
    private val searchService: SearchService,
) {
    suspend fun searchParties(
        keyword: String,
        page: Int,
        size: Int,
    ): BaseResponse<PartySearchResponseDto> =
        searchService.searchParties(keyword, page, size)
}
