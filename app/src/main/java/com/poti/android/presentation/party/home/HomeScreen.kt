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
import com.poti.android.core.common.util.screenHeightDp
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.button.PotiFloatingButton
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPrimary
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.home.Banner
import com.poti.android.domain.model.home.GroupItem
import com.poti.android.domain.model.home.HomeContent
import com.poti.android.presentation.party.home.component.HomeBannerSection
import com.poti.android.presentation.party.home.component.HomeGoodsSection
import com.poti.android.presentation.party.home.model.HomeUiEffect

val fakeMyGroupItems = listOf(
    GroupItem(
        postTitle = "2026 시즌 콘서트 후드",
        artist = "아이유",
        postImage = "",
        postCount = 3,
        tag = "인기",
    ),
    GroupItem(
        postTitle = "공식 응원봉 Ver.2",
        artist = "아이유",
        postImage = "",
        postCount = 12,
        tag = "NEW",
    ),
    GroupItem(
        postTitle = "월드투어 포토북",
        artist = "아이유",
        postImage = "",
        postCount = 7,
        tag = "",
    ),
)

@Composable
fun HomeRoute(
    onNavigateToPartyCreate: () -> Unit,
    onNavigateToPartyDetail: (Long) -> Unit,
    onNavigateToGoodsPartyList: () -> Unit,
    onNavigateToGoodsCategory: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            HomeUiEffect.NavigateToPartyCreate -> onNavigateToPartyCreate()
            is HomeUiEffect.NavigateToPartyDetail -> onNavigateToPartyDetail(effect.postId)
            HomeUiEffect.NavigateToGoodsPartyList -> onNavigateToGoodsPartyList()
            HomeUiEffect.NavigateToGoodsCategory -> onNavigateToGoodsCategory()
        }
    }

    uiState.homeContentLoadState.onSuccess { homeContent ->
        HomeScreen(
            homeContent = homeContent,
            onFloatingClick = onNavigateToPartyCreate,
            onBannerClick = onNavigateToPartyDetail,
            onMoreClick = onNavigateToGoodsPartyList,
            onCardClick = onNavigateToGoodsCategory,
            modifier = modifier,
        )
    }
}

@Composable
private fun HomeScreen(
    homeContent: HomeContent,
    onFloatingClick: () -> Unit,
    onBannerClick: (Long) -> Unit,
    onMoreClick: () -> Unit,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Column {
            PotiHeaderPrimary(
                firstIconRes = R.drawable.ic_search,
                onFirstIconClick = {},
                secondIconRes = R.drawable.ic_alarm,
                onSecondIconClick = {},
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(bottom = 48.dp),
            ) {
                HomeBannerSection(
                    banners = homeContent.banners,
                    onBannerClick = onBannerClick,
                    modifier = Modifier
                        .padding(top = screenHeightDp(16.dp))
                        .padding(horizontal = screenWidthDp(16.dp)),
                )

                Spacer(modifier = Modifier.height(screenHeightDp(36.dp)))

                HomeGoodsSection(
                    title = R.string.home_recommend_goods,
                    nickname = homeContent.nickname,
                    groupItems = homeContent.myGroupItems,
                    onMoreClick = onMoreClick,
                    onCardClick = onCardClick,
                )

                Spacer(modifier = Modifier.height(28.dp))

                HomeGoodsSection(
                    title = R.string.home_other_goods,
                    nickname = homeContent.nickname,
                    groupItems = homeContent.otherGroupItems,
                    onMoreClick = onMoreClick,
                    onCardClick = onCardClick,
                )
            }
        }

        PotiFloatingButton(
            onClick = onFloatingClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = screenWidthDp(20.dp),
                    bottom = screenHeightDp(12.dp),
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    PotiTheme {
        HomeScreen(
            homeContent = HomeContent(
                nickname = "포티",
                banners = listOf(
                    Banner(1, ""),
                    Banner(2, ""),
                    Banner(3, ""),
                ),
                myGroupItems = fakeMyGroupItems,
                otherGroupItems = fakeMyGroupItems,
            ),
            onFloatingClick = { },
            onBannerClick = { },
            onMoreClick = { },
            onCardClick = { },
        )
    }
}
