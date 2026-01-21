package com.poti.android.presentation.history.recruiter

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.repository.GroupBuyRepository
import com.poti.android.presentation.history.navigation.HistoryRoute
import com.poti.android.presentation.history.recruiter.model.RecruiterDetailUiEffect
import com.poti.android.presentation.history.recruiter.model.RecruiterDetailUiIntent
import com.poti.android.presentation.history.recruiter.model.RecruiterDetailUiState
import com.poti.android.presentation.history.recruiter.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecruiterViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val groupBuyRepository: GroupBuyRepository
) : BaseViewModel<RecruiterDetailUiState, RecruiterDetailUiIntent, RecruiterDetailUiEffect>(
        initialState = RecruiterDetailUiState(),
    ) {
    private val recruitId: Long = savedStateHandle.toRoute<HistoryRoute.RecruiterDetail>().recruitId

    init {
        if (recruitId != -1L) {
            getRecruiterDetail(recruitId)
        } else {
            updateState { copy(recruiterDetailState = ApiState.Init) }
        }
    }

    override fun processIntent(intent: RecruiterDetailUiIntent) {
        when (intent) {
            is RecruiterDetailUiIntent.BackButtonClicked -> sendEffect(RecruiterDetailUiEffect.NavigateBack)
            is RecruiterDetailUiIntent.PartyCardClicked -> sendEffect(RecruiterDetailUiEffect.NavigateToPartyDetail(recruitId))
            is RecruiterDetailUiIntent.ParticipantSectionClicked -> sendEffect(RecruiterDetailUiEffect.NavigateToParticipantList(recruitId))
        }
    }

    private fun getRecruiterDetail(recruitId: Long) = launchScope {
        groupBuyRepository.getPostSale(recruitId)
            .onSuccess {
                updateState {
                    copy(recruiterDetailState = ApiState.Success(it.toUiModel()))
                }
            }
            .onFailure { error ->
                updateState {
                    copy(
                        recruiterDetailState =
                            ApiState.Failure(error.message ?: "failed: getRecruiterDetail")
                    )
                }
            }
    }
}
