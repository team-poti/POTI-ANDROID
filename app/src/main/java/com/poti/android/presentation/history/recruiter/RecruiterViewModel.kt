package com.poti.android.presentation.history.recruiter

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.usecase.history.GetRecruitDetailUseCase
import com.poti.android.presentation.history.navigation.HistoryRoute
import com.poti.android.presentation.history.recruiter.model.RecruiterDetailUiEffect
import com.poti.android.presentation.history.recruiter.model.RecruiterDetailUiEffect.*
import com.poti.android.presentation.history.recruiter.model.RecruiterDetailUiIntent
import com.poti.android.presentation.history.recruiter.model.RecruiterDetailUiState
import com.poti.android.presentation.history.recruiter.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecruiterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRecruitDetailUseCase: GetRecruitDetailUseCase,
) : BaseViewModel<RecruiterDetailUiState, RecruiterDetailUiIntent, RecruiterDetailUiEffect>(
        initialState = RecruiterDetailUiState(),
    ) {
    private val recruitId: Long = savedStateHandle.toRoute<HistoryRoute.RecruiterDetail>().recruitId

    init {
        getRecruiterDetail()
    }

    override fun processIntent(intent: RecruiterDetailUiIntent) {
        when (intent) {
            is RecruiterDetailUiIntent.BackButtonClicked -> sendEffect(RecruiterDetailUiEffect.NavigateBack)
            is RecruiterDetailUiIntent.PartyCardClicked -> sendEffect(NavigateToPartyDetail(recruitId))
            is RecruiterDetailUiIntent.ParticipantSectionClicked -> sendEffect(NavigateToParticipantList(recruitId))
            RecruiterDetailUiIntent.OnResume -> getRecruiterDetail()
        }
    }

    private fun getRecruiterDetail() = launchScope {
        getRecruitDetailUseCase(recruitId)
            .onSuccess {
                updateState {
                    copy(recruiterDetailState = ApiState.Success(it.toUiModel()))
                }
            }
            .onFailure { error ->
                updateState {
                    copy(
                        recruiterDetailState =
                            ApiState.Failure(error.message ?: "failed: getRecruiterDetail"),
                    )
                }
            }
    }
}
