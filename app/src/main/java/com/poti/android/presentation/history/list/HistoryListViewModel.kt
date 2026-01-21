package com.poti.android.presentation.history.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.designsystem.component.navigation.PotiHeaderTabType
import com.poti.android.domain.model.history.HistoryItem
import com.poti.android.domain.model.history.HistoryListContent
import com.poti.android.domain.repository.ParticipationRepository
import com.poti.android.domain.repository.PartyRepository
import com.poti.android.domain.type.HistoryListType
import com.poti.android.presentation.history.list.model.HistoryListUiEffect
import com.poti.android.presentation.history.list.model.HistoryListUiIntent
import com.poti.android.presentation.history.list.model.HistoryListUiState
import com.poti.android.presentation.history.list.model.HistoryMode
import com.poti.android.presentation.history.navigation.HistoryRoute
import com.poti.android.presentation.user.component.HistorySummaryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryListViewModel @Inject constructor(
    private val participationRepository: ParticipationRepository,
    private val partyRepository: PartyRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<HistoryListUiState, HistoryListUiIntent, HistoryListUiEffect>(
    initialState = HistoryListUiState(),
) {

    private val route = savedStateHandle.toRoute<HistoryRoute.HistoryList>()
    private val initialMode = route.mode
    private val initialType = route.type

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

    //enum class HistorySummaryType {
    //    ALL,
    //    IN_PROGRESS,
    //    COMPLETED,
    //}

    // ONGOIDN / COMP;LETED
    init {
        updateState {
            copy(
                mode = initialMode ?: this.mode,
                selectedTab = when (initialType) {
                    null -> this.selectedTab
                    HistorySummaryType.COMPLETED -> PotiHeaderTabType.ENDED
                    else -> PotiHeaderTabType.ONGOING
                },
            )
        }

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
                selectedTab = PotiHeaderTabType.ONGOING,
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
            val requestStatus = when (uiState.value.selectedTab) {
                PotiHeaderTabType.ONGOING -> HistoryListType.IN_PROGRESS
                PotiHeaderTabType.ENDED -> HistoryListType.COMPLETED
            }

            val result = if (uiState.value.mode == HistoryMode.RECRUIT) {
                partyRepository.getMyRecruitList(requestStatus.name)
            } else {
                participationRepository.getMyParticipationList(requestStatus.name)
            }

            result.onSuccess { myPartyList ->
                val listStatus = myPartyList.currentState
                val content = HistoryListContent(
                    ongoingCount = myPartyList.inProgressCount,
                    endedCount = myPartyList.completedCount,
                    items = myPartyList.partyList.map { item ->
                        HistoryItem(
                            id = item.groupBuyId,
                            imageUrl = item.thumbnailUrl,
                            artist = item.artistName,
                            title = item.productName,
                            status = listStatus,
                        )
                    },
                )

                updateState {
                    copy(historyListLoadState = ApiState.Success(content))
                }
            }.onFailure { error ->
                updateState {
                    copy(historyListLoadState = ApiState.Failure(error.message ?: "Failed"))
                }
            }
        }
    }
}
