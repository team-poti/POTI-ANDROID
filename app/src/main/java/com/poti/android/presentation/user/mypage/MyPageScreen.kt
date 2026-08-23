package com.poti.android.presentation.user.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.extension.onSuccess
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPrimary
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.user.HistorySummary
import com.poti.android.domain.model.user.UserMyPage
import com.poti.android.presentation.history.list.model.HistoryMode
import com.poti.android.presentation.user.component.BadgeButton
import com.poti.android.presentation.user.component.HistorySummaryCard
import com.poti.android.presentation.user.component.HistorySummaryType
import com.poti.android.presentation.user.component.InquirySection
import com.poti.android.presentation.user.component.RatingBadge
import com.poti.android.presentation.user.component.UserInfo
import com.poti.android.presentation.user.component.UserProfile
import com.poti.android.presentation.user.mypage.model.MyPageUiEffect
import com.poti.android.presentation.user.mypage.model.MyPageUiIntent

@Composable
fun MyPageRoute(
    onNavigateToHistoryList: (HistoryMode, HistorySummaryType) -> Unit,
    onNavigateToFavoriteArtist: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current
    val inquiryUrl = stringResource(R.string.user_inquiry_url)

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.processIntent(MyPageUiIntent.OnResume)
    }

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            is MyPageUiEffect.NavigateToHistoryList -> {
                onNavigateToHistoryList(effect.mode, effect.tab)
            }

            MyPageUiEffect.NavigateToFavoriteArtist -> {
                onNavigateToFavoriteArtist()
            }
        }
    }

    uiState.userMyPageLoadState.onSuccess { userMyPage ->
        MyPageScreen(
            userMyPage = userMyPage,
            onArtistClick = { viewModel.processIntent(MyPageUiIntent.OnArtistClick) },
            onInquiryClick = { uriHandler.openUri(inquiryUrl) },
            onHistoryClick = { mode, type ->
                viewModel.processIntent(
                    MyPageUiIntent.OnHistoryClick(mode, type),
                )
            },
            modifier = modifier,
        )
    }
}

@Composable
private fun MyPageScreen(
    userMyPage: UserMyPage,
    onArtistClick: () -> Unit,
    onInquiryClick: () -> Unit,
    onHistoryClick: (HistoryMode, HistorySummaryType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val biasText = userMyPage.favoriteArtistName ?: stringResource(R.string.user_select_favorite_artist)

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        PotiHeaderPrimary(
            title = stringResource(R.string.user_my_page_title),
            firstIconRes = R.drawable.ic_setting,
            onFirstIconClick = {}, // TODO [천민재] 설정 페이지 이동처리
            secondIconRes = R.drawable.ic_alarm,
            onSecondIconClick = {}, // TODO [천민재] 알람페이지 이동처리
            containerColor = PotiTheme.colors.gray100,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PotiTheme.colors.gray100)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(PotiTheme.colors.white)
                    .padding(horizontal = 12.dp)
                    .padding(top = 57.dp, bottom = 37.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                UserProfile(
                    imageUrl = userMyPage.profileImageUrl,
                    nickname = userMyPage.nickname,
                    email = userMyPage.email,
                )

                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RatingBadge(
                        rating = userMyPage.ratingAvg,
                    )

                    BadgeButton(
                        bias = biasText,
                        onClick = onArtistClick,
                        modifier = Modifier,
                    )
                }
            }

            UserInfo(
                activityMessage = userMyPage.activityMessage,
                joinedAt = userMyPage.joinedAt,
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                HistorySummaryCard(
                    title = stringResource(R.string.user_history_participate),
                    summary = userMyPage.participationSummary,
                    onItemClick = { type -> onHistoryClick(HistoryMode.PARTICIPATION, type) },
                    modifier = Modifier.weight(1f),
                )

                HistorySummaryCard(
                    title = stringResource(R.string.user_history_recruit),
                    summary = userMyPage.recruitSummary,
                    onItemClick = { type -> onHistoryClick(HistoryMode.RECRUIT, type) },
                    modifier = Modifier.weight(1f),
                )
            }

            InquirySection(
                onInquiryClick = onInquiryClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    PotiTheme {
        MyPageScreen(
            userMyPage = UserMyPage(
                nickname = "분철의 악마",
                email = "akkma@app.jam",
                profileImageUrl = "",
                ratingAvg = "4.8",
                activityMessage = "최근 3일 이내 활동",
                joinedAt = "2025-12-28",
                hasFavoriteArtist = true,
                favoriteArtistName = "아이브(ive)",
                participationSummary = HistorySummary(
                    inProgress = 3,
                    completed = 9,
                ),
                recruitSummary = HistorySummary(
                    inProgress = 2,
                    completed = 5,
                ),
            ),
            onArtistClick = {},
            onInquiryClick = {},
            onHistoryClick = { _, _ -> },
            modifier = Modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview2() {
    PotiTheme {
        MyPageScreen(
            userMyPage = UserMyPage(
                nickname = "분철의 악마",
                email = "akkma@app.jam",
                profileImageUrl = "",
                ratingAvg = "4.8",
                activityMessage = "최근 3일 이내 활동",
                joinedAt = "2025-12-28",
                hasFavoriteArtist = true,
                favoriteArtistName = null,
                participationSummary = HistorySummary(
                    inProgress = 3,
                    completed = 9,
                ),
                recruitSummary = HistorySummary(
                    inProgress = 2,
                    completed = 5,
                ),
            ),
            onArtistClick = {},
            onInquiryClick = {},
            onHistoryClick = { _, _ -> },
            modifier = Modifier,
        )
    }
}
