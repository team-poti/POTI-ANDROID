package com.poti.android.presentation.user.profile

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
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
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.user.component.HistoryItemUiModel
import com.poti.android.presentation.user.component.HistorySummaryCard
import com.poti.android.presentation.user.component.HistorySummaryType
import com.poti.android.presentation.user.component.RatingBadge
import com.poti.android.presentation.user.component.UserInfo
import com.poti.android.presentation.user.component.UserProfile

data class HistorySummaryUiModel(
    val totalCount: Int,
    val inProgressCount: Int,
    val finishedCount: Int,
)

@Composable
fun ProfileScreenRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    val uiState = ProfileUiState(
        imageUrl = "",
        nickname = "포티포티포티",
        email = "poti@app.jam",
        rating = "4.8",
        infoList = listOf("최근 3일 이내 활동", "2025년 12월 28일 가입"),
        recruitHistory = HistorySummaryUiModel(
            totalCount = 7,
            inProgressCount = 2,
            finishedCount = 5,
        ),
    )

    ProfileScreen(
        uiState = uiState,
        scrollState = scrollState,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@Composable
private fun ProfileScreen(
    uiState: ProfileUiState,
    scrollState: ScrollState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val historyItems = listOf(
        HistoryItemUiModel(
            type = HistorySummaryType.ALL,
            titleRes = R.string.user_history_all,
            count = uiState.recruitHistory.totalCount,
        ),
        HistoryItemUiModel(
            type = HistorySummaryType.IN_PROGRESS,
            titleRes = R.string.user_history_ongoing,
            count = uiState.recruitHistory.inProgressCount,
        ),
        HistoryItemUiModel(
            type = HistorySummaryType.FINISHED,
            titleRes = R.string.user_history_ended,
            count = uiState.recruitHistory.finishedCount,
        ),
    )

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        PotiHeaderPage(
            onNavigationClick = onBackClick,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    horizontal = screenWidthDp(16.dp),
                    vertical = screenHeightDp(20.dp),
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            UserProfile(
                imageUrl = uiState.imageUrl,
                nickname = uiState.nickname,
                email = uiState.email,
            )

            Spacer(Modifier.height(24.dp))

            RatingBadge(
                rating = uiState.rating,
            )

            Spacer(Modifier.height(24.dp))

            UserInfo(
                infoList = uiState.infoList,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(24.dp))

            HistorySummaryCard(
                title = "모집 내역",
                items = historyItems,
                onItemClick = { type -> },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    val scrollState = rememberScrollState()

    val uiState = ProfileUiState(
        imageUrl = "",
        nickname = "포티포티포티",
        email = "poti@app.jam",
        rating = "4.8",
        infoList = listOf("최근 3일 이내 활동", "2025년 12월 28일 가입"),
        recruitHistory = HistorySummaryUiModel(7, 2, 5),
    )

    PotiTheme {
        ProfileScreen(
            uiState = uiState,
            scrollState = scrollState,
            onBackClick = {},
            modifier = Modifier,
        )
    }
}
