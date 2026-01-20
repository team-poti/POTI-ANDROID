package com.poti.android.presentation.history.participant.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState

data class ParticipantDetailUiState(
    val participantDetailState: ApiState<ParticipantDetailUiModel> = ApiState.Loading,
) : UiState

sealed interface ParticipantDetailUiIntent : UiIntent {
    data class LoadDetail(val recruitId: Long) : ParticipantDetailUiIntent

    data object OnBackClick : ParticipantDetailUiIntent

    data object OnPartyDetailClick : ParticipantDetailUiIntent

    data class SubmitDeposit(val depositor: String, val depositTime: String) : ParticipantDetailUiIntent

    data object ConfirmDelivery : ParticipantDetailUiIntent

    data class SubmitReview(val rating: Int) : ParticipantDetailUiIntent

    data object SkipReview : ParticipantDetailUiIntent
}

sealed interface ParticipantDetailUiEffect : UiEffect {
    data object NavigateBack : ParticipantDetailUiEffect

    data class NavigateToPartyDetail(val recruitId: Long) : ParticipantDetailUiEffect
}
