package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.search.toDomain
import com.poti.android.data.mock.UiMockData
import com.poti.android.data.mock.executeWithUiMock
import com.poti.android.data.remote.datasource.SearchRemoteDataSource
import com.poti.android.domain.model.search.PartySearchItem
import com.poti.android.domain.model.search.PartySearchResult
import com.poti.android.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val searchRemoteDataSource: SearchRemoteDataSource,
) : SearchRepository {
    override suspend fun searchParties(
        keyword: String,
        page: Int,
        size: Int,
    ): Result<PartySearchResult> = executeWithUiMock(
        mock = {
            val matchedItems = UiMockData.productCategory.groupItems
                .filter { item ->
                    item.artist.contains(keyword, ignoreCase = true) ||
                        item.postTitle.contains(keyword, ignoreCase = true)
                }
            val pageItems = matchedItems
                .drop(page * size)
                .take(size)

            PartySearchResult(
                items = pageItems.map { item ->
                    PartySearchItem(
                        artist = item.artist,
                        artistId = item.artistId,
                        postImage = item.postImage,
                        postTitle = item.postTitle,
                        postCount = item.postCount,
                        tag = item.tag,
                    )
                },
                hasNext = (page + 1) * size < matchedItems.size,
            )
        },
        real = {
            httpResponseHandler.safeApiCall {
                searchRemoteDataSource.searchParties(keyword, page, size)
                    .handleApiResponse()
                    .getOrThrow()
                    .toDomain()
            }
        },
    )
}
