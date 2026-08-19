package com.poti.android.presentation.party.search

import androidx.lifecycle.viewModelScope
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.usecase.search.SearchPartyUseCase
import com.poti.android.presentation.party.search.model.NextPageLoadState
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
                PartySearchUiIntent.OnBackClick -> sendEffect(PartySearchUiEffect.NavigateBack)
                is PartySearchUiIntent.OnCardClick -> sendEffect(
                    PartySearchUiEffect.NavigateToProductPartyList(
                        artistId = intent.artistId,
                        title = intent.title,
                    ),
                )
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
                    nextPageLoadState = NextPageLoadState.Idle,
                    hasNextPage = false,
                    nextPage = 0,
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
            if (state.nextPageLoadState != NextPageLoadState.Idle || !state.hasNextPage) return

            val trimmedKeyword = state.searchKeyword.trim()
            if (trimmedKeyword.isEmpty()) return

            updateState { copy(nextPageLoadState = NextPageLoadState.Loading) }

            searchJob = viewModelScope.launch {
                requestSearch(
                    keyword = trimmedKeyword,
                    page = state.nextPage,
                    reset = false,
                )
            }
        }

        private fun retryNextPage() {
            if (uiState.value.nextPageLoadState != NextPageLoadState.Failure) return

            updateState { copy(nextPageLoadState = NextPageLoadState.Idle) }
            loadNextPage()
        }

        private fun resetSearch(keyword: String) {
            updateState {
                copy(
                    searchKeyword = keyword,
                    searchResultLoadState = ApiState.Init,
                    nextPageLoadState = NextPageLoadState.Idle,
                    hasNextPage = false,
                    nextPage = 0,
                )
            }
        }

        private suspend fun requestSearch(
            keyword: String,
            page: Int,
            reset: Boolean,
        ) {
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
                            nextPageLoadState = NextPageLoadState.Idle,
                            hasNextPage = result.hasNext,
                            nextPage = page + 1,
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
                            nextPageLoadState = if (reset) {
                                NextPageLoadState.Idle
                            } else {
                                NextPageLoadState.Failure
                            },
                        )
                    }
                }
        }
    }
