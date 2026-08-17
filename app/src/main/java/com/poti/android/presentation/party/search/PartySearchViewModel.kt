package com.poti.android.presentation.party.search

import androidx.lifecycle.viewModelScope
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.usecase.search.SearchPartyUseCase
import com.poti.android.presentation.party.search.model.PartySearchUiEffect
import com.poti.android.presentation.party.search.model.PartySearchUiIntent
import com.poti.android.presentation.party.search.model.PartySearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val PARTY_SEARCH_PAGE_SIZE = 20
private const val PARTY_SEARCH_DEBOUNCE_MILLIS = 400L

@HiltViewModel
class PartySearchViewModel @Inject constructor(
    private val searchPartyUseCase: SearchPartyUseCase,
) :
    BaseViewModel<PartySearchUiState, PartySearchUiIntent, PartySearchUiEffect>(
            initialState = PartySearchUiState(),
        ) {
        private var searchJob: Job? = null

        override fun processIntent(intent: PartySearchUiIntent) {
            when (intent) {
                is PartySearchUiIntent.OnSearchKeywordChange -> scheduleSearch(intent.keyword)
                is PartySearchUiIntent.OnSearch -> scheduleSearch(intent.keyword, debounceMillis = 0L)
                PartySearchUiIntent.OnLoadNextPage -> loadNextPage()
                PartySearchUiIntent.OnRetryNextPage -> retryNextPage()
            }
        }

        private fun scheduleSearch(
            keyword: String,
            debounceMillis: Long = PARTY_SEARCH_DEBOUNCE_MILLIS,
        ) {
            searchJob?.cancel()

            val trimmedKeyword = keyword.trim()
            if (trimmedKeyword.isEmpty()) {
                resetSearch(keyword)
                return
            }

            updateState {
                copy(
                    searchKeyword = keyword,
                    searchResultLoadState = ApiState.Loading,
                    isPageLoading = false,
                    hasNextPage = false,
                    nextPage = 0,
                    isNextPageLoadFailed = false,
                )
            }

            searchJob = viewModelScope.launch {
                delay(debounceMillis)
                requestSearch(
                    keyword = trimmedKeyword,
                    page = 0,
                    reset = true,
                )
            }
        }

        private fun loadNextPage() {
            val state = uiState.value
            if (state.isPageLoading || !state.hasNextPage || state.isNextPageLoadFailed) return

            val trimmedKeyword = state.searchKeyword.trim()
            if (trimmedKeyword.isEmpty()) return

            searchJob = viewModelScope.launch {
                requestSearch(
                    keyword = trimmedKeyword,
                    page = state.nextPage,
                    reset = false,
                )
            }
        }

        private fun retryNextPage() {
            if (!uiState.value.isNextPageLoadFailed) return

            updateState { copy(isNextPageLoadFailed = false) }
            loadNextPage()
        }

        private fun resetSearch(keyword: String) {
            updateState {
                copy(
                    searchKeyword = keyword,
                    searchResultLoadState = ApiState.Init,
                    isPageLoading = false,
                    hasNextPage = false,
                    nextPage = 0,
                    isNextPageLoadFailed = false,
                )
            }
        }

        private suspend fun requestSearch(
            keyword: String,
            page: Int,
            reset: Boolean,
        ) {
            updateState { copy(isPageLoading = true) }

            searchPartyUseCase(
                keyword = keyword,
                page = page,
                size = PARTY_SEARCH_PAGE_SIZE,
            )
                .onSuccess { result ->
                    val currentItems = (uiState.value.searchResultLoadState as? ApiState.Success)
                        ?.data
                        ?.items
                        .orEmpty()
                    val updatedItems = if (reset) result.items else currentItems + result.items

                    updateState {
                        copy(
                            searchResultLoadState = ApiState.Success(
                                result.copy(
                                    items = updatedItems.distinctBy { item ->
                                        item.artistId to item.postTitle
                                    },
                                ),
                            ),
                            isPageLoading = false,
                            hasNextPage = result.hasNext,
                            nextPage = page + 1,
                            isNextPageLoadFailed = false,
                        )
                    }
                }
                .onFailure { throwable ->
                    updateState {
                        copy(
                            searchResultLoadState = if (reset) {
                                ApiState.Failure(throwable.message ?: "Failed to search parties")
                            } else {
                                searchResultLoadState
                            },
                            isPageLoading = false,
                            isNextPageLoadFailed = !reset,
                        )
                    }
                }
        }
    }
