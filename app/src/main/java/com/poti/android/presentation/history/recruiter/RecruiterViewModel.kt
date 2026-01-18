package com.poti.android.presentation.history.recruiter

import androidx.lifecycle.SavedStateHandle
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.presentation.history.DummyParticipantManageDetail
import com.poti.android.presentation.history.model.RecruiterDetailUiEffect
import com.poti.android.presentation.history.model.RecruiterDetailUiIntent
import com.poti.android.presentation.history.model.RecruiterDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecruiterViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<RecruiterDetailUiState, RecruiterDetailUiIntent, RecruiterDetailUiEffect>(
        initialState = RecruiterDetailUiState(),
    ) {
    private val recruitId: Long = savedStateHandle["recruitId"] ?: -1L

    init {
        if (recruitId != -1L) {
            getParticipantManageDetail(recruitId)
        } else {
            updateState { copy(recruiterDetail = ApiState.Loading) }
        }
    }

    override fun processIntent(intent: RecruiterDetailUiIntent) {
        when (intent) {
            is RecruiterDetailUiIntent.BackButtonClicked -> {
                sendEffect(RecruiterDetailUiEffect.NavigateBack)
            }
            is RecruiterDetailUiIntent.PartyCardClicked -> {
                sendEffect(RecruiterDetailUiEffect.NavigateToPartyDetail(recruitId))
            }
            is RecruiterDetailUiIntent.ParticipantSectionClicked -> {
                sendEffect(RecruiterDetailUiEffect.NavigateToParticipantList(recruitId))
            }
        }
    }

    private fun getParticipantManageDetail(recruitId: Long) {
        launchScope(
            onError = { throwable ->
                updateState {
                    copy(
                        recruiterDetail = ApiState.Failure(
                            "Fail: ${throwable.message}",
                        ),
                    )
                }
            },
        ) {
            updateState {
                // TODO: [천민재] 서버 연결 필요
                copy(
                    recruiterDetail = ApiState.Success(
                        DummyParticipantManageDetail.recruiterRecruitStep,
                    ),
                )
            }
        }
    }
}
