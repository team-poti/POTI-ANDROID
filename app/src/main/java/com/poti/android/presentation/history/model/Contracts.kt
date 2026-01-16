package com.poti.android.presentation.history.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.history.RecruiterDetail

data class RecruiterDetailUiState(
    val recruiterDetail: ApiState<RecruiterDetail> = ApiState.Loading,
) : UiState

sealed interface RecruiterDetailUiIntent : UiIntent {
    object BackButtonClicked : RecruiterDetailUiIntent
    object PartyCardClicked : RecruiterDetailUiIntent
    object ParticipantSectionClicked : RecruiterDetailUiIntent
}

sealed interface RecruiterDetailUiEffect : UiEffect {
    object NavigateBack : RecruiterDetailUiEffect
    data class NavigateToPartyDetail(val partyId: Long) : RecruiterDetailUiEffect
    data class NavigateToParticipantList(val partyId: Long) : RecruiterDetailUiEffect
}
