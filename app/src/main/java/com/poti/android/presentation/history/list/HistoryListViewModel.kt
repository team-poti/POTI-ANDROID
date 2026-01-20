package com.poti.android.presentation.history.list

import androidx.lifecycle.viewModelScope
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.designsystem.component.navigation.PotiHeaderTabType
import com.poti.android.domain.model.history.HistoryItem
import com.poti.android.domain.model.history.HistoryListContent
import com.poti.android.domain.type.HistoryListType
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
            HistoryListUiIntent.OnSwitchModeClick -> switchMode()
            is HistoryListUiIntent.OnTabSelected -> selectTab(intent.tab)
            is HistoryListUiIntent.OnCardClick -> {
                if (uiState.value.mode == HistoryMode.RECRUIT) {
                    sendEffect(HistoryListUiEffect.NavigateToRecruiterDetail(intent.id))
                } else {
                    sendEffect(HistoryListUiEffect.NavigateToParticipantDetail(intent.id))
                }
            }
        }
    }

    init {
        loadUserHistoryList()
    }

    private fun switchMode() {
        val newMode = if (uiState.value.mode == HistoryMode.RECRUIT) {
            HistoryMode.PARTICIPATION
        } else {
            HistoryMode.RECRUIT
        }

        updateState {
            copy(
                mode = newMode,
                selectedTab = PotiHeaderTabType.ONGOING, // 초기 탭 재설정
            )
        }

        loadUserHistoryList()
    }

    private fun selectTab(tab: PotiHeaderTabType) {
        updateState { copy(selectedTab = tab) }
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
                status = HistoryListType.IN_PROGRESS,
            ),
            HistoryItem(
                id = 2L,
                imageUrl = "",
                artist = "NewJeans",
                title = "OMG 한정판",
                status = HistoryListType.IN_PROGRESS,
            ),
        )

        val endedItems = listOf(
            HistoryItem(
                id = 3L,
                imageUrl = "",
                artist = "LE SSERAFIM",
                title = "ANTIFRAGILE",
                status = HistoryListType.COMPLETED,
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
