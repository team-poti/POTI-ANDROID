package com.poti.android.presentation.history.participant.model

sealed interface ParticipantDetailOverlayState {
    data object None : ParticipantDetailOverlayState

    data object DepositBottomSheet : ParticipantDetailOverlayState

    data object DeliveryConfirmModal : ParticipantDetailOverlayState

    data class DeliveryReviewModal(
        val recruiterName: String,
        val recruiterProfileUrl: String?,
        val partnerRating: String,
    ) : ParticipantDetailOverlayState
}
