package com.poti.android.presentation.party.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.common.extension.onSuccess
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPageSearch
import com.poti.android.data.mock.UiMockData
import com.poti.android.presentation.party.home.component.GoodsLargeCard
import com.poti.android.presentation.party.search.model.PartySearchUiState

@Composable
fun PartySearchRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PartySearchScreen(
        uiState = PartySearchUiState(),
        onBackClick = onBackClick,
        onCardClick = { _, _ -> },
        onSearchKeywordChange = {},
        onSearch = {},
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
    modifier: Modifier = Modifier,
) {
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
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
        ) {
            uiState.productCategoryLoadState.onSuccess { goodsCategory ->
                items(goodsCategory.groupItems) { groupItem ->
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
        productCategoryLoadState = ApiState.Success(
            UiMockData.productCategory,
        ),
    )
    PartySearchScreen(
        uiState = uiState,
        onBackClick = {},
        onSearchKeywordChange = {},
        onSearch = {},
        onCardClick = { _, _ -> },
    )
}
