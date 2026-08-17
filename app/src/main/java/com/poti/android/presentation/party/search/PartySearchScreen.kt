package com.poti.android.presentation.party.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.extension.onSuccess
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.designsystem.component.display.PotiEmptyStateInline
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPageSearch
import com.poti.android.data.mock.UiMockData
import com.poti.android.domain.model.search.PartySearchItem
import com.poti.android.domain.model.search.PartySearchResult
import com.poti.android.presentation.party.home.component.GoodsLargeCard
import com.poti.android.presentation.party.search.model.NextPageLoadState
import com.poti.android.presentation.party.search.model.PartySearchUiEffect
import com.poti.android.presentation.party.search.model.PartySearchUiIntent
import com.poti.android.presentation.party.search.model.PartySearchUiState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter

@Composable
fun PartySearchRoute(
    onBackClick: () -> Unit,
    onNavigateToProductPartyList: (Long, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PartySearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            PartySearchUiEffect.NavigateBack -> onBackClick()
            is PartySearchUiEffect.NavigateToProductPartyList -> {
                onNavigateToProductPartyList(effect.artistId, effect.title)
            }
        }
    }

    PartySearchScreen(
        uiState = uiState,
        onBackClick = { viewModel.processIntent(PartySearchUiIntent.OnBackClick) },
        onCardClick = { artistId, title ->
            viewModel.processIntent(PartySearchUiIntent.OnCardClick(artistId, title))
        },
        onSearchKeywordChange = { keyword -> viewModel.processIntent(PartySearchUiIntent.OnSearchKeywordChange(keyword)) },
        onSearch = { keyword -> viewModel.processIntent(PartySearchUiIntent.OnSearch(keyword)) },
        onLoadNextPage = { viewModel.processIntent(PartySearchUiIntent.OnLoadNextPage) },
        onRetryNextPage = { viewModel.processIntent(PartySearchUiIntent.OnRetryNextPage) },
        modifier = modifier,
    )
}

@Composable
fun PartySearchScreen(
    uiState: PartySearchUiState,
    onBackClick: () -> Unit,
    onCardClick: (Long, String) -> Unit,
    onSearchKeywordChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onLoadNextPage: () -> Unit,
    onRetryNextPage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val shouldLoadNextPage by remember(
        listState,
        uiState.hasNextPage,
        uiState.nextPageLoadState,
    ) {
        derivedStateOf {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: return@derivedStateOf false
            val totalItemsCount = listState.layoutInfo.totalItemsCount

            uiState.hasNextPage &&
                uiState.nextPageLoadState == NextPageLoadState.Idle &&
                totalItemsCount > 0 &&
                lastVisibleItemIndex >= totalItemsCount - 3
        }
    }

    LaunchedEffect(shouldLoadNextPage) {
        if (shouldLoadNextPage) {
            onLoadNextPage()
        }
    }

    LaunchedEffect(listState, uiState.nextPageLoadState) {
        if (uiState.nextPageLoadState != NextPageLoadState.Failure) return@LaunchedEffect

        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .drop(1)
            .filter { isScrolling -> isScrolling }
            .collect {
                val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                    ?: return@collect
                val totalItemsCount = listState.layoutInfo.totalItemsCount
                val isNearEnd = totalItemsCount > 0 && lastVisibleItemIndex >= totalItemsCount - 3

                if (isNearEnd) {
                    onRetryNextPage()
                }
            }
    }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        PotiHeaderPageSearch(
            onNavigationClick = onBackClick,
            value = uiState.searchKeyword,
            onValueChange = onSearchKeywordChange,
            onSearch = onSearch,
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
        ) {
            uiState.searchResultLoadState.onSuccess { searchResult ->
                if (searchResult.items.isEmpty()) {
                    item {
                        PotiEmptyStateInline(
                            text = stringResource(R.string.search_message_empty_result),
                        )
                    }
                } else {
                    items(searchResult.items) { groupItem ->
                        GoodsLargeCard(
                            imageUrl = groupItem.postImage,
                            artist = groupItem.artist,
                            title = groupItem.postTitle,
                            partyCount = groupItem.postCount,
                            tag = groupItem.tag,
                            onClick = onCardClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            artistId = groupItem.artistId,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PartySearchScreenPreview() {
    val uiState = PartySearchUiState(
        searchResultLoadState = ApiState.Success(
            PartySearchResult(
                items = UiMockData.productCategory.groupItems.map { item ->
                    PartySearchItem(
                        artist = item.artist,
                        artistId = item.artistId,
                        postImage = item.postImage,
                        postTitle = item.postTitle,
                        postCount = item.postCount.toLong(),
                        tag = item.tag,
                    )
                },
                hasNext = false,
            ),
        ),
    )
    PartySearchScreen(
        uiState = uiState,
        onBackClick = {},
        onSearchKeywordChange = {},
        onSearch = {},
        onLoadNextPage = {},
        onRetryNextPage = {},
        onCardClick = { _, _ -> },
    )
}
