package com.poti.android.presentation.history.participant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.designsystem.component.button.ActionButtonType
import com.poti.android.core.designsystem.component.button.PotiActionButton
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.history.ParticipantDetail
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.DummyParticipantManageDetail
import com.poti.android.presentation.history.component.DepositInfoSection
import com.poti.android.presentation.history.component.HistoryCalloutInfo
import com.poti.android.presentation.history.component.HistoryDeliveryConfirmModal
import com.poti.android.presentation.history.component.HistoryDeliveryReviewModal
import com.poti.android.presentation.history.component.HistoryDepositBottomSheet
import com.poti.android.presentation.history.component.HistoryParticipantStateLabel
import com.poti.android.presentation.history.component.ParticipantStateLabelSize
import com.poti.android.presentation.history.component.ParticipantStateLabelStage
import com.poti.android.presentation.history.component.ParticipantStateLabelStatus
import com.poti.android.presentation.history.component.PartyInfoSection
import com.poti.android.presentation.history.component.ProgressStatusSection
import com.poti.android.presentation.history.component.ShippingInfoSection
import com.poti.android.presentation.history.mapper.toUiState
import com.poti.android.presentation.history.model.participant.ParticipantDetailUiEffect
import com.poti.android.presentation.history.model.participant.ParticipantDetailUiIntent

private sealed interface ParticipantDetailModalState {
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
fun ParticipantDetailRoute(
    onBackClick: () -> Unit,
    onNavigateToPartyDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ParticipantViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                ParticipantDetailUiEffect.NavigateBack -> onBackClick()
                is ParticipantDetailUiEffect.NavigateToPartyDetail -> onNavigateToPartyDetail(effect.recruitId)
            }
        }
    }

    when (val state = uiState.participantDetail) {
        is ApiState.Success -> {
            ParticipantDetailScreen(
                modifier = modifier,
                detail = state.data,
                onDetailClick = { viewModel.processIntent(ParticipantDetailUiIntent.OnPartyDetailClick) },
                onBackClick = { viewModel.processIntent(ParticipantDetailUiIntent.OnBackClick) },
                onDepositSubmit = { depositor, time ->
                    viewModel.processIntent(ParticipantDetailUiIntent.SubmitDeposit(depositor, time))
                },
                onDeliveryConfirm = {
                    viewModel.processIntent(ParticipantDetailUiIntent.ConfirmDelivery)
                },
                onReviewSubmit = { rating ->
                    viewModel.processIntent(ParticipantDetailUiIntent.SubmitReview(rating))
                },
                onReviewSkip = {
                    viewModel.processIntent(ParticipantDetailUiIntent.SkipReview)
                },
            )
        }
        else -> Unit
    }
}

@Composable
private fun ParticipantDetailScreen(
    detail: ParticipantDetail,
    onBackClick: () -> Unit,
    onDetailClick: (Long) -> Unit,
    onDepositSubmit: (depositor: String, depositTime: String) -> Unit,
    onDeliveryConfirm: () -> Unit,
    onReviewSubmit: (Int) -> Unit,
    onReviewSkip: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var modalState by remember { mutableStateOf<ParticipantDetailModalState>(ParticipantDetailModalState.None) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            PotiHeaderPage(
                onNavigationClick = onBackClick,
                title = stringResource(id = R.string.history_participant_detail_title),
                modifier = Modifier.padding(top = 16.dp),
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(PotiTheme.colors.white),
        ) {
            item {
                PartyInfoSection(
                    recruitId = detail.recruitId,
                    artistInfo = detail.artistInfo,
                    onDetailClick = onDetailClick,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                ProgressStatusSection(
                    progressInfo = detail.progressInfo,
                    modifier = Modifier.padding(
                        top = 20.dp,
                        start = 16.dp,
                        end = 16.dp,
                    ),
                )
            }

            item {
                PotiDivider(
                    styleType = PotiDividerStyle.LARGE,
                    modifier = Modifier.padding(vertical = 20.dp),
                )
            }

            item {
                DepositInfoSection(info = detail.depositInfo)
            }

            item {
                PotiDivider(
                    styleType = PotiDividerStyle.LARGE,
                    modifier = Modifier.padding(top = 24.dp),
                )
            }

            item {
                ShippingInfoSection(
                    info = detail.shippingInfo,
                    modifier = Modifier.padding(top = 20.dp),
                )

                val (stage, state) = detail.userState.toUiState()

                if (stage == ParticipantStateLabelStage.DELIVERY) {
                    if (state == ParticipantStateLabelStatus.START) {
                        HistoryCalloutInfo(
                            text = detail.shippingInfo.trackingNumber ?: "",
                            copyable = true,
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .padding(horizontal = 16.dp),
                        )
                    }

                    Spacer(
                        Modifier.height(
                            if (state == ParticipantStateLabelStatus.START) {
                                12.dp
                            } else {
                                20.dp
                            },
                        ),
                    )

                    HistoryParticipantStateLabel(
                        sizeType = ParticipantStateLabelSize.LARGE,
                        stageType = stage,
                        statusType = state,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp)
                            .wrapContentWidth(Alignment.End),
                    )
                }

                Spacer(Modifier.height(24.dp))
            }

            if (detail.userState == ParticipantStatusType.RECRUIT_DONE ||
                detail.userState == ParticipantStatusType.DELIVERY_START
            ) {
                item {
                    PotiActionButton(
                        text = if (detail.userState == ParticipantStatusType.RECRUIT_DONE) {
                            stringResource(R.string.history_deposit_done_button)
                        } else {
                            stringResource(R.string.history_delivery_done_button)
                        },
                        onClick = {
                            modalState = when (detail.userState) {
                                ParticipantStatusType.RECRUIT_DONE -> ParticipantDetailModalState.DepositInput
                                ParticipantStatusType.DELIVERY_START -> ParticipantDetailModalState.DeliveryConfirm
                                else -> ParticipantDetailModalState.None
                            }
                        },
                        type = ActionButtonType.SECONDARY_MAIN,
                        enabled = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 14.dp),
                    )
                }
            }
        }
    }
    when (modalState) {
        ParticipantDetailModalState.None -> Unit

        ParticipantDetailModalState.DepositInput -> {
            HistoryDepositBottomSheet(
                onDismissRequest = { modalState = ParticipantDetailModalState.None },
                onConfirmClick = { depositor, time ->
                    modalState = ParticipantDetailModalState.None
                    onDepositSubmit(depositor, time)
                },
            )
        }

        ParticipantDetailModalState.DeliveryConfirm -> {
            HistoryDeliveryConfirmModal(
                onConfirm = {
                    modalState = ParticipantDetailModalState.DeliveryReview(
                        recruiterName = detail.recruiterName,
                        recruiterProfileUrl = detail.recruiterProfileUrl,
                        recruiterRating = detail.recruiterRating,
                    )
                    onDeliveryConfirm()
                },
                onDismiss = { modalState = ParticipantDetailModalState.None },
            )
        }

        is ParticipantDetailModalState.DeliveryReview -> {
            val deliveryReview = (modalState as ParticipantDetailModalState.DeliveryReview)
            HistoryDeliveryReviewModal(
                partnerNickname = deliveryReview.recruiterName,
                partnerProfileUrl = deliveryReview.recruiterProfileUrl,
                partnerRating = deliveryReview.recruiterRating,
                onConfirm = { rating ->
                    modalState = ParticipantDetailModalState.None
                    onReviewSubmit(rating)
                },
                onSkip = {
                    modalState = ParticipantDetailModalState.None
                    onReviewSkip()
                },
                onDismissRequest = { modalState = ParticipantDetailModalState.None },
            )
        }
    }
}

class ParticipantDetailPreviewProvider : PreviewParameterProvider<ParticipantDetail> {
    override val values: Sequence<ParticipantDetail> = sequenceOf(
        DummyParticipantManageDetail.participantDetailWaitDeposit,
        DummyParticipantManageDetail.participantDetailCheckDeposit,
        DummyParticipantManageDetail.participantDetailDeliveryStart,
    )
}

@Preview(showBackground = true)
@Composable
private fun ParticipantDetailScreenPreview(
    @PreviewParameter(ParticipantDetailPreviewProvider::class) detail: ParticipantDetail,
) {
    PotiTheme {
        ParticipantDetailScreen(
            detail = detail,
            onBackClick = {},
            onDetailClick = {},
            onDepositSubmit = { _, _ -> },
            onDeliveryConfirm = {},
            onReviewSubmit = {},
            onReviewSkip = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
