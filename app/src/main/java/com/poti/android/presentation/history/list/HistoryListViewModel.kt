package com.poti.android.presentation.history.list

import androidx.lifecycle.viewModelScope
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.designsystem.component.navigation.PotiHeaderTabType
import com.poti.android.domain.model.history.HistoryItem
import com.poti.android.domain.model.history.HistoryListContent
import com.poti.android.presentation.history.component.ParticipantStateLabelStage
import com.poti.android.presentation.history.component.ParticipantStateLabelStatus
import com.poti.android.presentation.history.list.model.HistoryListUiEffect
import com.poti.android.presentation.history.list.model.HistoryListUiIntent
import com.poti.android.presentation.history.list.model.HistoryListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
                loadUserHistoryList()
            }
            is HistoryListUiIntent.OnTabSelected -> {
                updateState { copy(selectedTab = intent.tab) }
                loadUserHistoryList()
            }
            is HistoryListUiIntent.OnCardClick -> {
                sendEffect(HistoryListUiEffect.NavigateToDetail(intent.id))
            }
        }
    }

    init {
        loadUserHistoryList()
    }

    private var fetchJob: Job? = null

    private fun loadUserHistoryList() {
        fetchJob?.cancel()

        fetchJob = viewModelScope.launch {
            val dummyContent = createDummyContent()

            updateState {
                copy(
                    historyListLoadState = ApiState.Success(dummyContent),
                )
            }
        }
    }

    fun createDummyContent(): HistoryListContent {
        val isOngoing = uiState.value.selectedTab == PotiHeaderTabType.ONGOING
        val isRecruit = uiState.value.mode == HistoryMode.RECRUIT

        val ongoingItems = listOf(
            HistoryItem(
                id = 1L,
                imageUrl = "",
                artist = if (isRecruit) "IVE" else "aespa",
                title = if (isRecruit) "러브다이브 공동구매" else "걸스 앨범 분철",
                stage = ParticipantStateLabelStage.DELIVERY,
                status = ParticipantStateLabelStatus.WAIT,
            ),
            HistoryItem(
                id = 2L,
                imageUrl = "",
                artist = "NewJeans",
                title = "OMG 한정판",
                stage = ParticipantStateLabelStage.DEPOSIT,
                status = ParticipantStateLabelStatus.DONE,
            ),
        )

        val endedItems = listOf(
            HistoryItem(
                id = 3L,
                imageUrl = "",
                artist = "LE SSERAFIM",
                title = "ANTIFRAGILE",
                stage = ParticipantStateLabelStage.DELIVERY,
                status = ParticipantStateLabelStatus.DONE,
            ),
        )

        return HistoryListContent(
            ongoingCount = ongoingItems.size,
            endedCount = endedItems.size,
            items = if (isOngoing) ongoingItems else endedItems,
        )
    }

    // TODO: [예림] API 분기
    // mode == RECRUIT -> 모집내역 API
    // mode == PARTICIPATION -> 참여내역 API
    // selectedTab -> IN_PROGRESS / COMPLETED
}
