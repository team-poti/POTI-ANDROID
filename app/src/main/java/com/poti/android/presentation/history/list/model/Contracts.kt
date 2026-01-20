package com.poti.android.presentation.history.list.model

import com.poti.android.R
import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.designsystem.component.navigation.PotiHeaderTabType
import com.poti.android.domain.model.history.HistoryItem
import com.poti.android.domain.model.history.HistoryListContent
import com.poti.android.domain.type.HistoryStage
import com.poti.android.domain.type.HistoryStatus
import com.poti.android.presentation.history.component.ParticipantStateLabelStage
import com.poti.android.presentation.history.component.ParticipantStateLabelStatus
import com.poti.android.presentation.history.list.HistoryMode

data class HistoryListUiState(
    val historyListLoadState: ApiState<HistoryListContent> = ApiState.Init,
    val mode: HistoryMode = HistoryMode.RECRUIT,
    val selectedTab: PotiHeaderTabType = PotiHeaderTabType.ONGOING,
) : UiState {
    val titleRes = when (mode) {
        HistoryMode.RECRUIT -> R.string.user_history_recruit
        HistoryMode.PARTICIPATION -> R.string.user_history_participate
    }

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

    data class OnTabSelected(val tab: PotiHeaderTabType) : HistoryListUiIntent

    data class OnCardClick(val id: Long) : HistoryListUiIntent
}

sealed interface HistoryListUiEffect : UiEffect {
    data object NavigateBack : HistoryListUiEffect

    data class NavigateToDetail(val id: Long) : HistoryListUiEffect
}

fun HistoryItem.toUiStage(): ParticipantStateLabelStage = when (stage) {
    HistoryStage.DEPOSIT -> ParticipantStateLabelStage.DEPOSIT
    HistoryStage.DELIVERY -> ParticipantStateLabelStage.DELIVERY
    HistoryStage.RECRUIT -> ParticipantStateLabelStage.RECRUIT
}

fun HistoryItem.toUiStatus(): ParticipantStateLabelStatus = when (status) {
    HistoryStatus.WAIT -> ParticipantStateLabelStatus.WAIT
    HistoryStatus.CHECK -> ParticipantStateLabelStatus.CHECK
    HistoryStatus.START -> ParticipantStateLabelStatus.START
    HistoryStatus.DONE -> ParticipantStateLabelStatus.DONE
}
