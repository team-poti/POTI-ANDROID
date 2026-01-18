package com.poti.android.presentation.party.goodsfilter

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.button.PotiFloatingButton
import com.poti.android.core.designsystem.component.button.PotiSmallButton
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.party.GoodsCategory
import com.poti.android.presentation.party.home.component.GoodsLargeCard

@Composable
fun GoodsCategoryRoute(
    modifier: Modifier = Modifier,
) {
//    GoodsCategoryScreen(modifier = modifier)
}

@Composable
private fun GoodsCategoryScreen(
    goodsCategory: GoodsCategory,
    onBackClick: () -> Unit,
    onFloatingClick: () -> Unit,
    onSortFilterClick: () -> Unit,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = PotiTheme.colors.white,
        topBar = {
            PotiHeaderPage(
                onNavigationClick = onBackClick,
                title = goodsCategory.nickname, // TODO: [예림] 홈 스트링 리소스 사용
            )
        },
        floatingActionButton = {
            PotiFloatingButton(
                onClick = onFloatingClick, // 아티스트 입력 상태로 등록 화면 이동
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = screenWidthDp(16.dp)),
        ) {
            item {
                PotiSmallButton(
                    text = "인기순", // TODO: 바텀시트 연결
                    onClick = onSortFilterClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End),
                )
            }

            items(goodsCategory.groupItems) { groupItem ->
                GoodsLargeCard(
                    imageUrl = groupItem.postImage,
                    artist = groupItem.artist,
                    title = groupItem.postTitle,
                    partyCount = groupItem.postCount,
                    tag = groupItem.tag,
                    onClick = onCardClick, // 상세 화면 이동
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                )
            }
            item {
                Spacer(Modifier.height(80.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GoodsCategoryScreenPreview() {
    PotiTheme {
        GoodsCategoryScreen(
            goodsCategory = dummyGoodsCategory,
            onBackClick = {},
            onFloatingClick = {},
            onSortFilterClick = {},
            onCardClick = {},
        )
    }
}
