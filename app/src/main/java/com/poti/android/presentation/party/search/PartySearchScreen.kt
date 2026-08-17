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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.core.common.extension.onSuccess
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPageSearch
import com.poti.android.data.mock.UiMockData
import com.poti.android.domain.model.search.PartySearchItem
import com.poti.android.domain.model.search.PartySearchResult
import com.poti.android.presentation.party.home.component.GoodsLargeCard
import com.poti.android.presentation.party.search.model.PartySearchUiIntent
import com.poti.android.presentation.party.search.model.PartySearchUiState

@Composable
fun PartySearchRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PartySearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PartySearchScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onCardClick = { _, _ -> },
        onSearchKeywordChange = { keyword -> viewModel.processIntent(PartySearchUiIntent.OnSearchKeywordChange(keyword)) },
        onSearch = { keyword -> viewModel.processIntent(PartySearchUiIntent.OnSearch(keyword)) },
        onLoadNextPage = { viewModel.processIntent(PartySearchUiIntent.OnLoadNextPage) },
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
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val shouldLoadNextPage by remember(listState, uiState.hasNextPage, uiState.isPageLoading) {
        derivedStateOf {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: return@derivedStateOf false
            val totalItemsCount = listState.layoutInfo.totalItemsCount

            uiState.hasNextPage &&
                !uiState.isPageLoading &&
                totalItemsCount > 0 &&
                lastVisibleItemIndex >= totalItemsCount - 3
        }
    }

    LaunchedEffect(shouldLoadNextPage) {
        if (shouldLoadNextPage) {
            onLoadNextPage()
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
                items(searchResult.items) { groupItem ->
                    GoodsLargeCard(
                        imageUrl = groupItem.postImage,
                        artist = groupItem.artist,
                        title = groupItem.postTitle,
                        partyCount = groupItem.postCount,
                        tag = groupItem.tag,
                        onClick = { id, title -> onCardClick(groupItem.artistId, groupItem.postTitle) },
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
                        postCount = item.postCount,
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
        onCardClick = { _, _ -> },
    )
}
