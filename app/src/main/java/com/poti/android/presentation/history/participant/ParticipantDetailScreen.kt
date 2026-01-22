package com.poti.android.presentation.history.participant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.extension.onSuccess
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.button.ActionButtonType
import com.poti.android.core.designsystem.component.button.PotiActionButton
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.presentation.history.component.HistoryDetailContentHeader
import com.poti.android.presentation.history.component.PartyInfoSection
import com.poti.android.presentation.history.component.ProgressStatusSection
import com.poti.android.presentation.history.participant.component.DeliveryStatusContent
import com.poti.android.presentation.history.participant.component.DepositStatusContent
import com.poti.android.presentation.history.participant.component.HistoryDeliveryConfirmModal
import com.poti.android.presentation.history.participant.component.HistoryDeliveryReviewModal
import com.poti.android.presentation.history.participant.component.HistoryDepositBottomSheet
import com.poti.android.presentation.history.participant.model.ParticipantButtonState
import com.poti.android.presentation.history.participant.model.ParticipantDetailOverlayState
import com.poti.android.presentation.history.participant.model.ParticipantDetailUiEffect
import com.poti.android.presentation.history.participant.model.ParticipantDetailUiIntent
import com.poti.android.presentation.history.participant.model.ParticipantDetailUiModel

@Composable
fun ParticipantDetailRoute(
    onPopBackStack: () -> Unit,
    onNavigateToPartyDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ParticipantViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            ParticipantDetailUiEffect.NavigateBack -> onPopBackStack()
            is ParticipantDetailUiEffect.NavigateToPartyDetail -> onNavigateToPartyDetail(effect.partyId)
        }
    }

    uiState.participantDetailState.onSuccess { participantDetail ->
        ParticipantDetailScreen(
            participantDetail = participantDetail,
            overlayState = uiState.overlayState,
            onBackClick = onPopBackStack,
            onDetailClick = {
                // TODO: partyId가 모델에 추가되면 사용 (현재는 임시로 detail.participationId 사용하거나 수정 필요)
                viewModel.processIntent(ParticipantDetailUiIntent.OnPartyDetailClick(participantDetail.participationId))
            },
            onActionButtonClick = { buttonState ->
                if (buttonState == ParticipantButtonState.DEPOSIT_DONE) {
                    viewModel.processIntent(ParticipantDetailUiIntent.OnDepositCompleteClick)
                } else {
                    viewModel.processIntent(ParticipantDetailUiIntent.OnDeliveredClick)
                }
            },
            onOverlayClose = { viewModel.processIntent(ParticipantDetailUiIntent.CloseOverlay) },
            onSubmitDeposit = { depositor, depositTime ->
                viewModel.processIntent(ParticipantDetailUiIntent.SubmitDeposit(depositor, depositTime))
            },
            onConfirmDelivery = { viewModel.processIntent(ParticipantDetailUiIntent.ConfirmDelivery) },
            onSubmitReview = { rating -> viewModel.processIntent(ParticipantDetailUiIntent.SubmitReview(rating)) },
            onSkipReview = { viewModel.processIntent(ParticipantDetailUiIntent.SkipReview) },
            modifier = modifier,
        )
    }
}

@Composable
private fun ParticipantDetailScreen(
    participantDetail: ParticipantDetailUiModel,
    overlayState: ParticipantDetailOverlayState,
    onBackClick: () -> Unit,
    onDetailClick: () -> Unit,
    onActionButtonClick: (ParticipantButtonState) -> Unit,
    onOverlayClose: () -> Unit,
    onSubmitDeposit: (String, String) -> Unit,
    onConfirmDelivery: () -> Unit,
    onSubmitReview: (Int) -> Unit,
    onSkipReview: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        PotiHeaderPage(
            onNavigationClick = onBackClick,
            title = stringResource(id = R.string.history_ongoing_title),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 50.dp),
        ) {
            PartyInfoSection(
                orderNumber = participantDetail.orderNumber,
                partySummary = participantDetail.partySummary,
                onDetailClick = onDetailClick,
                modifier = Modifier.padding(horizontal = screenWidthDp(8.dp)),
            )

            ProgressStatusSection(
                progressStatus = participantDetail.partySummary.partyStatus,
                statusMessage = participantDetail.partySummary.statusMessage,
                modifier = Modifier.padding(horizontal = screenWidthDp(16.dp)),
            )

            PotiDivider(
                styleType = PotiDividerStyle.LARGE,
                modifier = Modifier.padding(top = 24.dp),
            )

            HistoryDetailContentHeader(text = stringResource(R.string.history_participant_field_type_deposit))

            DepositStatusContent(
                memberPayments = participantDetail.memberPayments,
                shippingInfo = participantDetail.shippingInfo,
                paymentInfo = participantDetail.paymentInfo,
                participantStatusType = participantDetail.paymentInfo.depositStatus,
            )

            PotiDivider(PotiDividerStyle.LARGE)

            HistoryDetailContentHeader(text = stringResource(R.string.history_shipping_info_title))

            DeliveryStatusContent(
                shippingInfo = participantDetail.shippingInfo,
                participantStatusType = participantDetail.shippingInfo.shippingStatus,
            )
        }

        when (participantDetail.buttonState) {
            ParticipantButtonState.DEPOSIT_DONE -> {
                PotiActionButton(
                    text = stringResource(R.string.history_deposit_done_button),
                    onClick = { onActionButtonClick(ParticipantButtonState.DEPOSIT_DONE) },
                    type = ActionButtonType.SECONDARY_MAIN,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = screenWidthDp(16.dp), vertical = 4.dp),
                )
            }
            ParticipantButtonState.DELIVERY_RECEIVED -> {
                PotiActionButton(
                    text = stringResource(R.string.history_delivery_done_button),
                    onClick = { onActionButtonClick(ParticipantButtonState.DELIVERY_RECEIVED) },
                    type = ActionButtonType.SECONDARY_MAIN,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = screenWidthDp(16.dp), vertical = 4.dp),
                )
            }
            ParticipantButtonState.NONE -> {}
        }
    }

    when (overlayState) {
        ParticipantDetailOverlayState.DepositBottomSheet -> {
            HistoryDepositBottomSheet(
                onDismissRequest = onOverlayClose,
                onConfirmClick = onSubmitDeposit,
            )
        }
        ParticipantDetailOverlayState.DeliveryConfirmModal -> {
            HistoryDeliveryConfirmModal(
                onConfirm = onConfirmDelivery,
                onDismiss = onOverlayClose,
            )
        }
        is ParticipantDetailOverlayState.DeliveryReviewModal -> {
            HistoryDeliveryReviewModal(
                recruiterName = overlayState.recruiterName,
                recruiterProfileUrl = overlayState.recruiterProfileUrl,
                partnerRating = overlayState.partnerRating,
                onConfirm = onSubmitReview,
                onSkip = onSkipReview,
                onDismissRequest = onOverlayClose,
            )
        }
        ParticipantDetailOverlayState.None -> {}
    }
}
