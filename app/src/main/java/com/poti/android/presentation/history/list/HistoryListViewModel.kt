package com.poti.android.presentation.history.list

import androidx.lifecycle.viewModelScope
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.designsystem.component.navigation.PotiHeaderTabType
import com.poti.android.presentation.history.component.ParticipantStateLabelStage
import com.poti.android.presentation.history.component.ParticipantStateLabelStatus
import com.poti.android.presentation.history.list.model.HistoryItem
import com.poti.android.presentation.history.list.model.HistoryListUiEffect
import com.poti.android.presentation.history.list.model.HistoryListUiIntent
import com.poti.android.presentation.history.list.model.HistoryListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryListViewModel @Inject constructor() : BaseViewModel<HistoryListUiState, HistoryListUiIntent, HistoryListUiEffect>(
    initialState = HistoryListUiState(),
) {
    override fun processIntent(intent: HistoryListUiIntent) {
        when (intent) {
            HistoryListUiIntent.OnBackClick -> sendEffect(HistoryListUiEffect.NavigateBack)
            HistoryListUiIntent.OnSwitchModeClick -> {
                val newMode = if (uiState.value.mode == HistoryMode.RECRUIT) {
                    HistoryMode.PARTICIPATION
                } else {
                    HistoryMode.RECRUIT
                }
                updateState {
                    copy(
                        mode = newMode,
                        selectedTab = PotiHeaderTabType.ONGOING,
                    )
                }
                loadHistory()
            }
            is HistoryListUiIntent.OnTabSelected -> {
                updateState { copy(selectedTab = intent.tab) }
                loadHistory()
            }
            is HistoryListUiIntent.OnCardClick -> {
                sendEffect(HistoryListUiEffect.NavigateToDetail(intent.id))
            }
        }
    }

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            // TODO: [예림] API 분기
            // mode == RECRUIT -> 모집내역 API
            // mode == PARTICIPATION -> 참여내역 API
            // selectedTab -> IN_PROGRESS / COMPLETED

            val dummyItems = if (uiState.value.selectedTab == PotiHeaderTabType.ONGOING) {
                listOf(
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
                )
            } else {
                listOf()
            }

            updateState {
                copy(
                    isLoading = false,
                    ongoingCount = 2,
                    endedCount = 0,
                    items = dummyItems,
                )
            }
        }
    }
}
