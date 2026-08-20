package com.poti.android.presentation.history.list.model

import com.poti.android.R
import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.designsystem.component.navigation.PotiHeaderTabType
import com.poti.android.domain.model.history.HistoryItem
import com.poti.android.domain.model.history.HistoryListContent

data class HistoryListUiState(
    val historyListLoadState: ApiState<HistoryListContent> = ApiState.Init,
    val mode: HistoryMode = HistoryMode.RECRUIT,
    val selectedTab: PotiHeaderTabType = PotiHeaderTabType.ONGOING,
    /** 하단 내비게이션으로 진입했는지 여부입니다. true면 뒤로가기 없이 모드 토글 헤더를 사용합니다. */
    val isRootEntry: Boolean = false,
) : UiState {
    val titleRes = when (mode) {
        HistoryMode.RECRUIT -> R.string.user_history_recruit
        HistoryMode.PARTICIPATION -> R.string.user_history_participate
    }

    val isRecruitMode = mode == HistoryMode.RECRUIT

    val emptyTextRes = when (mode) {
        HistoryMode.RECRUIT -> {
            when (selectedTab) {
                PotiHeaderTabType.ONGOING -> R.string.history_empty_recruit_ongoing
                PotiHeaderTabType.ENDED -> R.string.history_empty_recruit_ended
            }
        }

        HistoryMode.PARTICIPATION -> {
            when (selectedTab) {
                PotiHeaderTabType.ONGOING -> R.string.history_empty_participation_ongoing
                PotiHeaderTabType.ENDED -> R.string.history_empty_participation_ended
            }
        }
    }
    val items: List<HistoryItem>
        get() = (historyListLoadState as? ApiState.Success)?.data?.items.orEmpty()

    val ongoingCount: Int
        get() = (historyListLoadState as? ApiState.Success)?.data?.ongoingCount ?: 0

    val endedCount: Int
        get() = (historyListLoadState as? ApiState.Success)?.data?.endedCount ?: 0
}

sealed interface HistoryListUiIntent : UiIntent {
    data object OnBackClick : HistoryListUiIntent

    data object OnSwitchModeClick : HistoryListUiIntent

    data class OnModeSelected(val mode: HistoryMode) : HistoryListUiIntent

    data class OnTabSelected(val tab: PotiHeaderTabType) : HistoryListUiIntent

    data class OnCardClick(val id: Long) : HistoryListUiIntent

    data object OnResume : HistoryListUiIntent
}

sealed interface HistoryListUiEffect : UiEffect {
    data object NavigateBack : HistoryListUiEffect

    data class NavigateToRecruiterDetail(val id: Long) : HistoryListUiEffect

    data class NavigateToParticipantDetail(val id: Long) : HistoryListUiEffect
}
