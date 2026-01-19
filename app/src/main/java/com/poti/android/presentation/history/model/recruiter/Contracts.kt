package com.poti.android.presentation.history.model.recruiter

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState

data class RecruiterDetailUiState(
    val recruiterDetail: ApiState<RecruiterDetailUiModel> = ApiState.Loading,
) : UiState

sealed interface RecruiterDetailUiIntent : UiIntent {
    object BackButtonClicked : RecruiterDetailUiIntent

    object PartyCardClicked : RecruiterDetailUiIntent

    object ParticipantSectionClicked : RecruiterDetailUiIntent
}

sealed interface RecruiterDetailUiEffect : UiEffect {
    object NavigateBack : RecruiterDetailUiEffect

    data class NavigateToPartyDetail(val recruitId: Long) : RecruiterDetailUiEffect

    data class NavigateToParticipantList(val recruitId: Long) : RecruiterDetailUiEffect
}
