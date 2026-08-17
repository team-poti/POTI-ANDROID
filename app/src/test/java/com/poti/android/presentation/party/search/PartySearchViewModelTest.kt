package com.poti.android.presentation.party.search

import com.poti.android.MainDispatcherRule
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.search.PartySearchItem
import com.poti.android.domain.model.search.PartySearchResult
import com.poti.android.domain.repository.SearchRepository
import com.poti.android.domain.usecase.search.SearchPartyUseCase
import com.poti.android.presentation.party.search.model.NextPageLoadState
import com.poti.android.presentation.party.search.model.PartySearchUiIntent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PartySearchViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var searchRepository: FakeSearchRepository
    private lateinit var viewModel: PartySearchViewModel

    @Before
    fun setUp() {
        searchRepository = FakeSearchRepository()
        viewModel = PartySearchViewModel(SearchPartyUseCase(searchRepository))
    }

    @Test
    fun `searches only the latest keyword after debounce`() =
        runTest(mainDispatcherRule.testDispatcher) {
            searchRepository.enqueue(Result.success(searchResult(item(1), hasNext = false)))

            viewModel.processIntent(PartySearchUiIntent.OnSearchKeywordChange("아"))
            advanceTimeBy(399)
            runCurrent()
            assertTrue(searchRepository.requests.isEmpty())

            viewModel.processIntent(PartySearchUiIntent.OnSearchKeywordChange("아이브"))
            advanceTimeBy(400)
            runCurrent()

            assertEquals(1, searchRepository.requests.size)
            assertEquals("아이브", searchRepository.requests.single().keyword)
        }

    @Test
    fun `clears results and cancels pending search when keyword becomes empty`() =
        runTest(mainDispatcherRule.testDispatcher) {
            viewModel.processIntent(PartySearchUiIntent.OnSearchKeywordChange("아이브"))
            viewModel.processIntent(PartySearchUiIntent.OnSearchKeywordChange(""))
            advanceUntilIdle()

            assertTrue(searchRepository.requests.isEmpty())
            assertEquals("", viewModel.uiState.value.searchKeyword)
            assertEquals(ApiState.Init, viewModel.uiState.value.searchResultLoadState)
            assertFalse(viewModel.uiState.value.hasNextPage)
            assertEquals(0, viewModel.uiState.value.nextPage)
        }

    @Test
    fun `search intent skips debounce and loads first page`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val firstItem = item(1)
            searchRepository.enqueue(Result.success(searchResult(firstItem, hasNext = true)))

            viewModel.processIntent(PartySearchUiIntent.OnSearch("  아이브  "))
            advanceUntilIdle()

            assertEquals(
                SearchRequest(keyword = "아이브", page = 0, size = 20),
                searchRepository.requests.single(),
            )
            val state = viewModel.uiState.value
            val result = (state.searchResultLoadState as ApiState.Success).data
            assertEquals(listOf(firstItem), result.items)
            assertTrue(state.hasNextPage)
            assertEquals(1, state.nextPage)
        }

    @Test
    fun `appends next page and removes duplicate items`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val firstItem = item(1)
            val secondItem = item(2)
            searchRepository.enqueue(Result.success(searchResult(firstItem, hasNext = true)))
            searchRepository.enqueue(Result.success(searchResult(firstItem, secondItem, hasNext = false)))

            viewModel.processIntent(PartySearchUiIntent.OnSearch("아이브"))
            advanceUntilIdle()
            viewModel.processIntent(PartySearchUiIntent.OnLoadNextPage)
            advanceUntilIdle()

            assertEquals(listOf(0, 1), searchRepository.requests.map { it.page })
            val state = viewModel.uiState.value
            val result = (state.searchResultLoadState as ApiState.Success).data
            assertEquals(listOf(firstItem, secondItem), result.items)
            assertFalse(state.hasNextPage)
            assertEquals(2, state.nextPage)
        }

    @Test
    fun `keeps current results after page failure and retries the same page`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val firstItem = item(1)
            val secondItem = item(2)
            searchRepository.enqueue(Result.success(searchResult(firstItem, hasNext = true)))
            searchRepository.enqueue(Result.failure(IllegalStateException("page failed")))
            searchRepository.enqueue(Result.success(searchResult(secondItem, hasNext = false)))

            viewModel.processIntent(PartySearchUiIntent.OnSearch("아이브"))
            advanceUntilIdle()
            viewModel.processIntent(PartySearchUiIntent.OnLoadNextPage)
            advanceUntilIdle()

            val failedState = viewModel.uiState.value
            val failedResult = (failedState.searchResultLoadState as ApiState.Success).data
            assertEquals(listOf(firstItem), failedResult.items)
            assertEquals(NextPageLoadState.Failure, failedState.nextPageLoadState)
            assertEquals(1, failedState.nextPage)

            viewModel.processIntent(PartySearchUiIntent.OnRetryNextPage)
            advanceUntilIdle()

            val retriedState = viewModel.uiState.value
            val retriedResult = (retriedState.searchResultLoadState as ApiState.Success).data
            assertEquals(listOf(firstItem, secondItem), retriedResult.items)
            assertEquals(listOf(0, 1, 1), searchRepository.requests.map { it.page })
            assertEquals(NextPageLoadState.Idle, retriedState.nextPageLoadState)
            assertFalse(retriedState.hasNextPage)
            assertEquals(2, retriedState.nextPage)
        }

    @Test
    fun `moves to failure when first page request fails`() =
        runTest(mainDispatcherRule.testDispatcher) {
            searchRepository.enqueue(Result.failure(IllegalStateException("search failed")))

            viewModel.processIntent(PartySearchUiIntent.OnSearch("아이브"))
            advanceUntilIdle()

            val state = viewModel.uiState.value
            val failure = state.searchResultLoadState as ApiState.Failure
            assertEquals("search failed", failure.message)
            assertEquals(NextPageLoadState.Idle, state.nextPageLoadState)
        }

    private fun item(id: Long) = PartySearchItem(
        artist = "artist-$id",
        artistId = id,
        postImage = "image-$id",
        postTitle = "title-$id",
        postCount = id,
        tag = null,
    )

    private fun searchResult(
        vararg items: PartySearchItem,
        hasNext: Boolean,
    ) = PartySearchResult(
        items = items.toList(),
        hasNext = hasNext,
    )

    private class FakeSearchRepository : SearchRepository {
        val requests = mutableListOf<SearchRequest>()
        private val results = ArrayDeque<Result<PartySearchResult>>()

        fun enqueue(result: Result<PartySearchResult>) {
            results.addLast(result)
        }

        override suspend fun searchParties(
            keyword: String,
            page: Int,
            size: Int,
        ): Result<PartySearchResult> {
            requests += SearchRequest(keyword, page, size)
            return results.removeFirst()
        }
    }

    private data class SearchRequest(
        val keyword: String,
        val page: Int,
        val size: Int,
    )
}
