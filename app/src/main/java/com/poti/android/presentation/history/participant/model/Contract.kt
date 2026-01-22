package com.poti.android.presentation.history.participant.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState

data class ParticipantDetailUiState(
    val participantDetailState: ApiState<ParticipantDetailUiModel> = ApiState.Loading,
    val overlayState: ParticipantDetailOverlayState = ParticipantDetailOverlayState.None,
) : UiState

sealed interface ParticipantDetailOverlayState {
    data object None : ParticipantDetailOverlayState

    data object DepositBottomSheet : ParticipantDetailOverlayState

    data object DeliveryConfirmModal : ParticipantDetailOverlayState

    data class DeliveryReviewModal(
        val recruiterName: String,
        val recruiterProfileUrl: String,
        val partnerRating: String,
    ) : ParticipantDetailOverlayState
}

sealed interface ParticipantDetailUiIntent : UiIntent {
    data class LoadDetail(val recruitId: Long) : ParticipantDetailUiIntent

    data object OnBackClick : ParticipantDetailUiIntent

    data class OnPartyDetailClick(val partyId: Long) : ParticipantDetailUiIntent

    data object OnDepositCompleteClick : ParticipantDetailUiIntent

    data object CloseOverlay : ParticipantDetailUiIntent

    data class SubmitDeposit(val depositor: String, val depositTime: String) : ParticipantDetailUiIntent

    data object OnDeliveredClick : ParticipantDetailUiIntent

    data object ConfirmDelivery : ParticipantDetailUiIntent

    data class SubmitReview(val rating: Int) : ParticipantDetailUiIntent

    data object SkipReview : ParticipantDetailUiIntent
}

sealed interface ParticipantDetailUiEffect : UiEffect {
    data object NavigateBack : ParticipantDetailUiEffect

    data class NavigateToPartyDetail(val partyId: Long) : ParticipantDetailUiEffect
}
