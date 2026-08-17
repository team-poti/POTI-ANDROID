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
import com.poti.android.domain.model.party.GroupItem
import com.poti.android.domain.model.party.ProductCategory
import com.poti.android.presentation.party.home.component.GoodsLargeCard
import com.poti.android.presentation.party.search.model.PartySearchUiState

@Composable
fun PartySearchRoute(modifier: Modifier = Modifier) {
}

@Composable
fun PartySearchScreen(
    uiState: PartySearchUiState,
    modifier: Modifier = Modifier,
    onCardClick: (Long, String) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        // TODO: 검색 컴포넌트 추가

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
            ProductCategory(
                nickname = "",
                mainArtist = "",
                mainArtistId = 1,
                groupItems = listOf(
                    GroupItem(
                        artist = "아티스트명",
                        artistId = 1,
                        postImage = "",
                        postTitle = "상품 종류명",
                        postCount = 2,
                        tag = "인기",
                    ),
                ),
                myGroupItems = listOf(),
            ),
        ),
    )
    PartySearchScreen(
        uiState = uiState,
        onCardClick = { _, _ -> },
    )
}
