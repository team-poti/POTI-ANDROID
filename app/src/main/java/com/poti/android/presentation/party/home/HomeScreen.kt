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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
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
    onNavigateToGoodsCategory: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeScreen(
        uiState = HomeContent(
            nickname = "포티",
            banners = listOf(
                Banner(1, ""),
                Banner(2, ""),
                Banner(3, ""),
            ),
            myGroupItems = fakeMyGroupItems,
            otherGroupItems = fakeMyGroupItems,
        ),
        onCardClick = onNavigateToGoodsCategory,
        onFloatingClick = { },
        modifier = modifier,
    )
}

@Composable
private fun HomeScreen(
    uiState: HomeContent,
    onCardClick: () -> Unit,
    onFloatingClick: () -> Unit,
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
                    banners = uiState.banners,
                    modifier = Modifier
                        .padding(top = screenHeightDp(16.dp))
                        .padding(horizontal = screenWidthDp(16.dp)),
                )

                Spacer(modifier = Modifier.height(screenHeightDp(36.dp)))

                HomeGoodsSection(
                    title = R.string.home_recommend_goods,
                    nickname = uiState.nickname,
                    groupItems = uiState.myGroupItems,
                    onCardClick = onCardClick,
                )

                Spacer(modifier = Modifier.height(28.dp))

                HomeGoodsSection(
                    title = R.string.home_other_goods,
                    nickname = uiState.nickname,
                    groupItems = uiState.otherGroupItems,
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
            uiState = HomeContent(
                nickname = "포티",
                banners = listOf(
                    Banner(1, ""),
                    Banner(2, ""),
                    Banner(3, ""),
                ),
                myGroupItems = fakeMyGroupItems,
                otherGroupItems = fakeMyGroupItems,
            ),
            onCardClick = { },
            onFloatingClick = { },
        )
    }
}
