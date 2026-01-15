package com.poti.android.presentation.user.profile

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.util.screenHeightDp
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.HistorySummaryItem
import com.poti.android.presentation.user.component.HistorySummaryCard
import com.poti.android.presentation.user.component.HistorySummaryType
import com.poti.android.presentation.user.component.RatingBadge
import com.poti.android.presentation.user.component.UserInfo
import com.poti.android.presentation.user.component.UserProfile
import com.poti.android.presentation.user.profile.model.ProfileUiState

@Composable
fun ProfileScreenRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val historyItems = listOf(
        HistorySummaryItem(
            type = HistorySummaryType.ALL,
            titleRes = R.string.user_history_all,
            count = 7,
        ),
        HistorySummaryItem(
            type = HistorySummaryType.IN_PROGRESS,
            titleRes = R.string.user_history_ongoing,
            count = 2,
        ),
        HistorySummaryItem(
            type = HistorySummaryType.FINISHED,
            titleRes = R.string.user_history_ended,
            count = 5,
        ),
    )

    val uiState = ProfileUiState(
        imageUrl = "",
        nickname = "포티포티포티",
        email = "poti@app.jam",
        rating = "4.8",
        infoList = listOf("최근 3일 이내 활동", "2025년 12월 28일 가입"),
        recruitHistoryItems = historyItems,
    )

    ProfileScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@Composable
private fun ProfileScreen(
    uiState: ProfileUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

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
                title = stringResource(R.string.user_history_recruit),
                items = uiState.recruitHistoryItems,
                onItemClick = { type -> },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    val historyItems = listOf(
        HistorySummaryItem(
            type = HistorySummaryType.ALL,
            titleRes = R.string.user_history_all,
            count = 7,
        ),
        HistorySummaryItem(
            type = HistorySummaryType.IN_PROGRESS,
            titleRes = R.string.user_history_ongoing,
            count = 2,
        ),
        HistorySummaryItem(
            type = HistorySummaryType.FINISHED,
            titleRes = R.string.user_history_ended,
            count = 5,
        ),
    )

    val uiState = ProfileUiState(
        imageUrl = "",
        nickname = "포티포티포티",
        email = "poti@app.jam",
        rating = "4.8",
        infoList = listOf("최근 3일 이내 활동", "2025년 12월 28일 가입"),
        recruitHistoryItems = historyItems,
    )

    PotiTheme {
        ProfileScreen(
            uiState = uiState,
            onBackClick = {},
            modifier = Modifier,
        )
    }
}
