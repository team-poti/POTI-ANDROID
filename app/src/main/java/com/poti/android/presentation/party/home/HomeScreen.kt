package com.poti.android.presentation.party.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.extension.onSuccess
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.designsystem.component.button.PotiFloatingButton
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPrimary
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.data.mock.UiMockData
import com.poti.android.domain.model.home.HomeContent
import com.poti.android.presentation.party.home.component.HomeBannerSection
import com.poti.android.presentation.party.home.component.HomeGoodsSection
import com.poti.android.presentation.party.home.model.HomeUiEffect
import com.poti.android.presentation.party.home.model.HomeUiIntent

@Composable
fun HomeRoute(
    onNavigateToPartySearch: () -> Unit,
    onNavigateToAlarmList: () -> Unit,
    onNavigateToPartyCreate: () -> Unit,
    onNavigateToGoodsPartyList: (Long, String) -> Unit,
    onNavigateToProductCategory: (Long?, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            HomeUiEffect.NavigateToPartySearch -> onNavigateToPartySearch()
            HomeUiEffect.NavigateToPartyCreate -> onNavigateToPartyCreate()
            is HomeUiEffect.NavigateToGoodsPartyList -> onNavigateToGoodsPartyList(effect.artistId, effect.title)
            is HomeUiEffect.NavigateToMyArtistCategory -> onNavigateToProductCategory(effect.artistId, true)
            HomeUiEffect.NavigateToOtherProductCategory -> onNavigateToProductCategory(null, false)
            HomeUiEffect.NavigateToAlarmList -> onNavigateToAlarmList()
        }
    }

    uiState.homeContentLoadState.onSuccess { homeContent ->
        HomeScreen(
            homeContent = homeContent,
            onSearchClick = { viewModel.processIntent(HomeUiIntent.OnSearchClick) },
            onFloatingClick = { viewModel.processIntent(HomeUiIntent.OnFloatingClick) },
            onMyArtistCategoryClick = { artistId -> viewModel.processIntent(HomeUiIntent.OnMyArtistCategoryClick(artistId)) },
            onOtherProductCategoryClick = { viewModel.processIntent(HomeUiIntent.OnOtherProductCategoryClick) },
            onProductCardClick = { artistId, title -> viewModel.processIntent(HomeUiIntent.OnProductCardClick(artistId, title)) },
            onAlarmClick = { viewModel.processIntent(HomeUiIntent.OnAlarmClick) },
            modifier = modifier,
        )
    }
}

@Composable
private fun HomeScreen(
    homeContent: HomeContent,
    onSearchClick: () -> Unit,
    onFloatingClick: () -> Unit,
    onMyArtistCategoryClick: (Long?) -> Unit,
    onOtherProductCategoryClick: (Long?) -> Unit,
    onProductCardClick: (Long, String) -> Unit,
    onAlarmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Column {
            PotiHeaderPrimary(
                firstIconRes = R.drawable.ic_search,
                onFirstIconClick = onSearchClick,
                secondIconRes = R.drawable.ic_alarm,
                onSecondIconClick = onAlarmClick,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(bottom = 48.dp),
            ) {
                HomeBannerSection(
                    banners = homeContent.banners,
                    onBannerClick = {},
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp),
                )

                Spacer(modifier = Modifier.height(36.dp))

                HomeGoodsSection(
                    artistId = homeContent.mainArtistId,
                    title = R.string.home_recommend_goods,
                    nickname = homeContent.nickname,
                    groupItems = homeContent.myGroupItems,
                    onMoreClick = onMyArtistCategoryClick,
                    onCardClick = onProductCardClick,
                )

                Spacer(modifier = Modifier.height(28.dp))

                HomeGoodsSection(
                    artistId = homeContent.mainArtistId,
                    title = R.string.home_other_goods,
                    nickname = homeContent.nickname,
                    groupItems = homeContent.otherGroupItems,
                    onMoreClick = onOtherProductCategoryClick,
                    onCardClick = onProductCardClick,
                )
            }
        }

        PotiFloatingButton(
            onClick = onFloatingClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 20.dp,
                    bottom = 12.dp,
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    PotiTheme {
        HomeScreen(
            onSearchClick = {},
            homeContent = UiMockData.homeContent,
            onFloatingClick = { },
            onMyArtistCategoryClick = { },
            onOtherProductCategoryClick = {},
            onProductCardClick = { _, _ -> },
            onAlarmClick = {},
        )
    }
}
