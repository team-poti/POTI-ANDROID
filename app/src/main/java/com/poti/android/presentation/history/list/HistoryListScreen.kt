package com.poti.android.presentation.history.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.designsystem.component.display.PotiEmptyStateInline
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.component.navigation.PotiHeaderSection
import com.poti.android.core.designsystem.component.navigation.PotiHeaderTabType
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.history.HistoryItem
import com.poti.android.domain.model.history.HistoryListContent
import com.poti.android.domain.type.HistoryListType
import com.poti.android.presentation.history.component.CardHistorySize
import com.poti.android.presentation.history.component.HistoryCardItem
import com.poti.android.presentation.history.list.model.HistoryListUiEffect
import com.poti.android.presentation.history.list.model.HistoryListUiIntent
import com.poti.android.presentation.history.list.model.HistoryListUiState
import com.poti.android.presentation.history.mapper.color
import com.poti.android.presentation.history.mapper.labelResId
import com.poti.android.presentation.history.mapper.statusColor

enum class HistoryMode {
    RECRUIT,
    PARTICIPATION,
}

@Composable
fun HistoryListRoute(
    onPopBackStack: () -> Unit,
    onNavigateToRecruiterDetail: (Long) -> Unit,
    onNavigateToParticipantDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            HistoryListUiEffect.NavigateBack -> onPopBackStack()
            is HistoryListUiEffect.NavigateToRecruiterDetail -> onNavigateToRecruiterDetail(effect.id)
            is HistoryListUiEffect.NavigateToParticipantDetail -> onNavigateToParticipantDetail(effect.id)
        }
    }

    HistoryListScreen(
        uiState = uiState,
        onBackClick = { viewModel.processIntent(HistoryListUiIntent.OnBackClick) },
        onSwitchModeClick = { viewModel.processIntent(HistoryListUiIntent.OnSwitchModeClick) },
        onTabSelected = { tab ->
            viewModel.processIntent(HistoryListUiIntent.OnTabSelected(tab))
        },
        onCardClick = { id ->
            viewModel.processIntent(HistoryListUiIntent.OnCardClick(id))
        },
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
    Scaffold(
        modifier = modifier,
        topBar = {
            PotiHeaderPage(
                onNavigationClick = onBackClick,
                title = stringResource(uiState.titleRes),
                onTrailingIconClick = onSwitchModeClick,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            PotiHeaderSection(
                selectedTab = uiState.selectedTab,
                ongoingCount = uiState.ongoingCount,
                endedCount = uiState.endedCount,
                onTabSelected = onTabSelected,
            )

            if (uiState.items.isEmpty()) {
                PotiEmptyStateInline(
                    text = stringResource(uiState.emptyTextRes),
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 12.dp,
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(
                        items = uiState.items,
                        key = { it.id },
                    ) { item ->
                        HistoryCardItem(
                            sizeType = CardHistorySize.SMALL,
                            imageUrl = item.imageUrl ?: "",
                            artist = item.artist,
                            title = item.title,
                            statusTextId = item.status.labelResId,
                            statusColor = item.status.statusColor.color,
                            onClick = { onCardClick(item.id) },
                        )
                    }
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
                historyListLoadState = ApiState.Success(
                    HistoryListContent(
                        ongoingCount = 2,
                        endedCount = 5,
                        items = listOf(
                            HistoryItem(
                                id = 1L,
                                imageUrl = "",
                                artist = "ive(아이브)",
                                title = "러브다이브 위드뮤",
                                status = HistoryListType.IN_PROGRESS,
                            ),
                            HistoryItem(
                                id = 2L,
                                imageUrl = "",
                                artist = "aespa",
                                title = "걸스 스페셜",
                                status = HistoryListType.COMPLETED,
                            ),
                        ),
                    ),
                ),
                selectedTab = PotiHeaderTabType.ONGOING,
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
                historyListLoadState = ApiState.Success(
                    HistoryListContent(
                        ongoingCount = 2,
                        endedCount = 0,
                        items = emptyList(),
                    ),
                ),
                selectedTab = PotiHeaderTabType.ENDED,
            ),
            onBackClick = {},
            onSwitchModeClick = {},
            onTabSelected = {},
            onCardClick = {},
        )
    }
}
