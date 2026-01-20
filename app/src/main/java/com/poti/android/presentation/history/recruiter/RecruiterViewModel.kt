package com.poti.android.presentation.history.recruiter

import androidx.lifecycle.SavedStateHandle
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.presentation.history.DummyParticipantManageDetail
<<<<<<< HEAD
import com.poti.android.presentation.history.recruiter.model.RecruiterDetailUiEffect
import com.poti.android.presentation.history.recruiter.model.RecruiterDetailUiIntent
import com.poti.android.presentation.history.recruiter.model.RecruiterDetailUiState
=======
import com.poti.android.presentation.history.model.recruiter.RecruiterDetailUiEffect
import com.poti.android.presentation.history.model.recruiter.RecruiterDetailUiIntent
import com.poti.android.presentation.history.model.recruiter.RecruiterDetailUiState
>>>>>>> 374aec2ca20e47732b2889c58ecb6c5e6fdb15f1
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
<<<<<<< HEAD
            updateState { copy(recruiterDetailState = ApiState.Loading) }
=======
            updateState { copy(recruiterDetail = ApiState.Loading) }
>>>>>>> 374aec2ca20e47732b2889c58ecb6c5e6fdb15f1
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
<<<<<<< HEAD
                        recruiterDetailState = ApiState.Failure(
=======
                        recruiterDetail = ApiState.Failure(
>>>>>>> 374aec2ca20e47732b2889c58ecb6c5e6fdb15f1
                            "Fail: ${throwable.message}",
                        ),
                    )
                }
            },
        ) {
            updateState {
                // TODO: [천민재] 서버 연결 필요
                copy(
<<<<<<< HEAD
                    recruiterDetailState = ApiState.Success(
=======
                    recruiterDetail = ApiState.Success(
>>>>>>> 374aec2ca20e47732b2889c58ecb6c5e6fdb15f1
                        DummyParticipantManageDetail.recruiterRecruitStep,
                    ),
                )
            }
        }
    }
}
