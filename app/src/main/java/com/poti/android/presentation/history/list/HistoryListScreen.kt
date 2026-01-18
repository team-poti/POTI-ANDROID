package com.poti.android.presentation.history.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.component.navigation.PotiHeaderSection
import com.poti.android.core.designsystem.component.navigation.PotiHeaderTabType
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.history.component.CardHistorySize
import com.poti.android.presentation.history.component.HistoryCardItem
import com.poti.android.presentation.history.component.ParticipantStateLabelStage
import com.poti.android.presentation.history.component.ParticipantStateLabelStatus
import com.poti.android.presentation.history.list.model.HistoryItem
import com.poti.android.presentation.history.list.model.HistoryListUiState

enum class HistoryMode {
    RECRUIT,
    PARTICIPATION,
}

@Composable
fun HistoryListRoute(
    onBackClick: () -> Unit = {},
    onSwitchModeClick: () -> Unit = {},
    onTabSelected: (PotiHeaderTabType) -> Unit = {},
    onCardClick: (Long) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    // TODO: [예림] ViewModel 연결되면 교체
    val uiState = HistoryListUiState(
        selectedTab = PotiHeaderTabType.ONGOING,
        ongoingCount = 2,
        endedCount = 5,
        items = listOf(
            HistoryItem(
                id = 1L,
                imageUrl = "",
                artist = "ive(아이브)",
                title = "러브다이브 위드뮤",
                stageType = ParticipantStateLabelStage.DELIVERY,
                statusType = ParticipantStateLabelStatus.WAIT,
            ),
            HistoryItem(
                id = 2L,
                imageUrl = "",
                artist = "aespa",
                title = "걸스 스페셜",
                stageType = ParticipantStateLabelStage.DEPOSIT,
                statusType = ParticipantStateLabelStatus.DONE,
            ),
        ),
    )

    HistoryListScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onSwitchModeClick = onSwitchModeClick,
        onTabSelected = onTabSelected,
        onCardClick = onCardClick,
        modifier = modifier,
    )
}

@Composable
private fun HistoryListScreen(
    uiState: HistoryListUiState,
    onBackClick: () -> Unit,
    onSwitchModeClick: () -> Unit,
    onTabSelected: (PotiHeaderTabType) -> Unit,
    onCardClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val titleRes = when (uiState.mode) {
        HistoryMode.RECRUIT -> R.string.user_history_recruit
        HistoryMode.PARTICIPATION -> R.string.user_history_participate
    }

    Scaffold(
        topBar = {
            PotiHeaderPage(
                onNavigationClick = onBackClick,
                title = stringResource(titleRes),
                onTrailingIconClick = onSwitchModeClick,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            PotiHeaderSection(
                selectedTab = uiState.selectedTab,
                ongoingCount = uiState.ongoingCount,
                endedCount = uiState.endedCount,
                onTabSelected = onTabSelected,
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 16.dp,
                    vertical = 12.dp,
                ),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp),
            ) {
                items(
                    items = uiState.items,
                    key = { it.id },
                ) { item ->

                    HistoryCardItem(
                        sizeType = CardHistorySize.SMALL,
                        imageUrl = item.imageUrl,
                        artist = item.artist,
                        title = item.title,
                        participantStageType = item.stageType,
                        participantStatusType = item.statusType,
                        onClick = { onCardClick(item.id) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryListScreenPreview_Ongoing() {
    PotiTheme {
        HistoryListScreen(
            uiState = HistoryListUiState(
                selectedTab = PotiHeaderTabType.ONGOING,
                ongoingCount = 2,
                endedCount = 5,
                items = listOf(
                    HistoryItem(
                        id = 1L,
                        imageUrl = "",
                        artist = "ive(아이브)",
                        title = "러브다이브 위드뮤",
                        stageType = ParticipantStateLabelStage.DELIVERY,
                        statusType = ParticipantStateLabelStatus.WAIT,
                    ),
                    HistoryItem(
                        id = 2L,
                        imageUrl = "",
                        artist = "aespa",
                        title = "걸스 스페셜",
                        stageType = ParticipantStateLabelStage.DEPOSIT,
                        statusType = ParticipantStateLabelStatus.DONE,
                    ),
                ),
            ),
            onBackClick = {},
            onSwitchModeClick = {},
            onTabSelected = {},
            onCardClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryListScreenPreview_Ended() {
    PotiTheme {
        HistoryListScreen(
            uiState = HistoryListUiState(
                selectedTab = PotiHeaderTabType.ENDED,
                ongoingCount = 2,
                endedCount = 5,
                items = listOf(
                    HistoryItem(
                        id = 3L,
                        imageUrl = "",
                        artist = "ive(아이브)",
                        title = "러브다이브 위드뮤",
                        stageType = ParticipantStateLabelStage.DELIVERY,
                        statusType = ParticipantStateLabelStatus.DONE,
                    ),
                ),
            ),
            onBackClick = {},
            onSwitchModeClick = {},
            onTabSelected = {},
            onCardClick = {},
        )
    }
}
