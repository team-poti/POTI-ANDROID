package com.poti.android.presentation.history.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.poti.android.core.designsystem.theme.PotiTheme

sealed interface ParticipantDetailModalState {
    data object None : ParticipantDetailModalState

    data object DepositInput : ParticipantDetailModalState

    data object DeliveryConfirm : ParticipantDetailModalState

    data class DeliveryReview(
        val recruiterName: String,
        val recruiterProfileUrl: String,
        val recruiterRating: String,
    ) : ParticipantDetailModalState
}

@Composable
fun ParticipantDetailDialogs(
    modalState: ParticipantDetailModalState,
    recruiterInfo: RecruiterInfoForReview,
    onDismiss: () -> Unit,
    onDepositSubmit: (String, String) -> Unit,
    onDeliveryConfirm: () -> Unit,
    onNavigateToReview: () -> Unit,
    onReviewSubmit: (Int) -> Unit,
    onReviewSkip: () -> Unit,
) {
    when (modalState) {
        ParticipantDetailModalState.None -> Unit

        ParticipantDetailModalState.DepositInput -> {
            HistoryDepositBottomSheet(
                onDismissRequest = onDismiss,
                onConfirmClick = { depositor, time ->
                    onDismiss()
                    onDepositSubmit(depositor, time)
                },
            )
        }

        ParticipantDetailModalState.DeliveryConfirm -> {
            HistoryDeliveryConfirmModal(
                onConfirm = {
                    onDeliveryConfirm()
                    onNavigateToReview()
                },
                onDismiss = onDismiss,
            )
        }

        is ParticipantDetailModalState.DeliveryReview -> {
            HistoryDeliveryReviewModal(
                partnerNickname = recruiterInfo.name,
                partnerProfileUrl = recruiterInfo.profileUrl,
                partnerRating = recruiterInfo.rating,
                onConfirm = { rating ->
                    onDismiss()
                    onReviewSubmit(rating)
                },
                onSkip = {
                    onDismiss()
                    onReviewSkip()
                },
                onDismissRequest = onDismiss,
            )
        }
    }
}

data class RecruiterInfoForReview(
    val name: String,
    val profileUrl: String,
    val rating: String,
)

@Preview
@Composable
private fun ParticipantDetailDialogsDepositPreview() {
    PotiTheme {
        ParticipantDetailDialogs(
            modalState = ParticipantDetailModalState.DepositInput,
            recruiterInfo = RecruiterInfoForReview("포티", "", "4.8"),
            onDismiss = {},
            onDepositSubmit = { _, _ -> },
            onDeliveryConfirm = {},
            onNavigateToReview = {},
            onReviewSubmit = {},
            onReviewSkip = {},
        )
    }
}

@Preview
@Composable
private fun ParticipantDetailDialogsConfirmPreview() {
    PotiTheme {
        ParticipantDetailDialogs(
            modalState = ParticipantDetailModalState.DeliveryConfirm,
            recruiterInfo = RecruiterInfoForReview("포티", "", "4.8"),
            onDismiss = {},
            onDepositSubmit = { _, _ -> },
            onDeliveryConfirm = {},
            onNavigateToReview = {},
            onReviewSubmit = {},
            onReviewSkip = {},
        )
    }
}

@Preview
@Composable
private fun ParticipantDetailDialogsReviewPreview() {
    PotiTheme {
        ParticipantDetailDialogs(
            modalState = ParticipantDetailModalState.DeliveryReview("포티", "", "4.8"),
            recruiterInfo = RecruiterInfoForReview("포티", "", "4.8"),
            onDismiss = {},
            onDepositSubmit = { _, _ -> },
            onDeliveryConfirm = {},
            onNavigateToReview = {},
            onReviewSubmit = {},
            onReviewSkip = {},
        )
    }
}
