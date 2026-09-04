package com.poti.android.presentation.history.recruiter.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState

data class RecruiterDetailUiState(
    val recruiterDetailState: ApiState<RecruiterDetailUiModel> = ApiState.Loading,
    val isDeleting: Boolean = false,
) : UiState

sealed interface RecruiterDetailUiIntent : UiIntent {
    data object BackButtonClicked : RecruiterDetailUiIntent

    data object PartyCardClicked : RecruiterDetailUiIntent

    data object ParticipantSectionClicked : RecruiterDetailUiIntent

    data object DeleteButtonClicked : RecruiterDetailUiIntent

    data object OnResume : RecruiterDetailUiIntent
}

sealed interface RecruiterDetailUiEffect : UiEffect {
    object NavigateBack : RecruiterDetailUiEffect

    data class NavigateToPartyDetail(val recruitId: Long) : RecruiterDetailUiEffect

    data class NavigateToParticipantList(val recruitId: Long) : RecruiterDetailUiEffect
}
