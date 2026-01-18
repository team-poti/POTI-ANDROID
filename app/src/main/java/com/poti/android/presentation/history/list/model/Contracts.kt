package com.poti.android.presentation.history.list.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.designsystem.component.navigation.PotiHeaderTabType
import com.poti.android.presentation.history.component.ParticipantStateLabelStage
import com.poti.android.presentation.history.component.ParticipantStateLabelStatus
import com.poti.android.presentation.history.list.HistoryMode

data class HistoryListUiState(
    val isLoading: Boolean = false,
    val mode: HistoryMode = HistoryMode.RECRUIT,
    val selectedTab: PotiHeaderTabType = PotiHeaderTabType.ONGOING,
    val ongoingCount: Int = 0,
    val endedCount: Int = 0,
    val items: List<HistoryItem> = emptyList(),
) : UiState

data class HistoryItem(
    val id: Long,
    val imageUrl: String,
    val artist: String,
    val title: String,
    val stageType: ParticipantStateLabelStage,
    val statusType: ParticipantStateLabelStatus,
)

sealed interface HistoryListUiIntent : UiIntent {
    data object OnBackClick : HistoryListUiIntent

    data object OnSwitchModeClick : HistoryListUiIntent

    data class OnTabSelected(val tab: PotiHeaderTabType) : HistoryListUiIntent

    data class OnCardClick(val id: Long) : HistoryListUiIntent
}

sealed interface HistoryListUiEffect : UiEffect {
    data object NavigateBack : HistoryListUiEffect

    data class SwitchMode(val isRecruitMode: Boolean) : HistoryListUiEffect

    data class NavigateToDetail(val id: Long) : HistoryListUiEffect
}
