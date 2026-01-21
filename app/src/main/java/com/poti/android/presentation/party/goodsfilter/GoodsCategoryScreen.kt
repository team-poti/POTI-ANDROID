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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.extension.onSuccess
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.button.PotiFloatingButton
import com.poti.android.core.designsystem.component.button.PotiSmallButton
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.party.GoodsCategory
import com.poti.android.presentation.party.goodsfilter.model.GoodsCategoryUiEffect
import com.poti.android.presentation.party.goodsfilter.model.GoodsCategoryUiIntent
import com.poti.android.presentation.party.home.component.GoodsLargeCard

@Composable
fun GoodsCategoryRoute(
    artistId: Long,
    onPopBackStack: () -> Unit,
    onNavigateToPartyCreate: () -> Unit,
    onNavigateToGoodsPartyList: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GoodsCategoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            GoodsCategoryUiEffect.NavigateBack -> onPopBackStack()
            GoodsCategoryUiEffect.NavigateToPartyCreate -> onNavigateToPartyCreate()
            GoodsCategoryUiEffect.NavigateToGoodsFilter -> onNavigateToGoodsPartyList(artistId)
        }
    }

    uiState.goodsCategoryLoadState.onSuccess { goodsCategory ->
        GoodsCategoryScreen(
            goodsCategory = goodsCategory,
            onBackClick = {
                viewModel.processIntent(GoodsCategoryUiIntent.OnBackClick)
            },
            onFloatingClick = {
                viewModel.processIntent(GoodsCategoryUiIntent.OnFloatingClick)
            },
            onSortFilterClick = {
                viewModel.processIntent(GoodsCategoryUiIntent.OnSortFilterClick)
            },
            onCardClick = {
                viewModel.processIntent(GoodsCategoryUiIntent.OnCardClick)
            },
            modifier = modifier,
        )
    }
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
                title = stringResource(R.string.home_recommend_goods, goodsCategory.nickname),
            )
        },
        floatingActionButton = {
            PotiFloatingButton(
                onClick = onFloatingClick, // 아티스트 입력 상태로 등록 화면 이동, 아티스트 아이디?
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
                    onClick = onCardClick, // 굿즈별 페이지로 이동 타이틀, 아티스트 아이디?
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
