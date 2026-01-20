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
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.designsystem.component.button.ActionButtonType
import com.poti.android.core.designsystem.component.button.PotiActionButton
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.history.DummyParticipantManageDetail
import com.poti.android.presentation.history.component.DepositInfoSection
import com.poti.android.presentation.history.component.HistoryCalloutInfo
import com.poti.android.presentation.history.component.HistoryParticipantStateLabel
import com.poti.android.presentation.history.component.ParticipantDetailDialogs
import com.poti.android.presentation.history.component.ParticipantDetailModalState
import com.poti.android.presentation.history.component.ParticipantStateLabelSize
import com.poti.android.presentation.history.component.PartyInfoSection
import com.poti.android.presentation.history.component.ProgressStatusSection
import com.poti.android.presentation.history.component.RecruiterInfoForReview
import com.poti.android.presentation.history.component.ShippingInfoSection
import com.poti.android.presentation.history.model.participant.ActionButtonState
import com.poti.android.presentation.history.model.participant.ParticipantDetailActionType
import com.poti.android.presentation.history.model.participant.ParticipantDetailUiEffect
import com.poti.android.presentation.history.model.participant.ParticipantDetailUiIntent
import com.poti.android.presentation.history.model.participant.ParticipantDetailUiModel

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
    detail: ParticipantDetailUiModel,
    onBackClick: () -> Unit,
    onDetailClick: () -> Unit,
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
                title = stringResource(id = detail.topBarTitleResId),
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
                    artistInfo = detail.partySummaryInfo,
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

                if (detail.isTrackingInfoVisible) {
                    HistoryCalloutInfo(
                        text = detail.shippingInfo.trackingNumber ?: "",
                        copyable = true,
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .padding(horizontal = 16.dp),
                    )
                }

                if (detail.isParticipantStatusVisible) {
                    Spacer(
                        Modifier.height(
                            if (detail.isTrackingInfoVisible) {
                                12.dp
                            } else {
                                20.dp
                            },
                        ),
                    )

                    HistoryParticipantStateLabel(
                        sizeType = ParticipantStateLabelSize.LARGE,
                        stageType = detail.userStage,
                        statusType = detail.userStatus,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp)
                            .wrapContentWidth(Alignment.End),
                    )
                }

                Spacer(Modifier.height(24.dp))
            }

            if (detail.actionButtonState is ActionButtonState.Visible) {
                val buttonState = detail.actionButtonState
                item {
                    PotiActionButton(
                        text = stringResource(buttonState.textResId),
                        onClick = {
                            modalState = when (buttonState.actionType) {
                                ParticipantDetailActionType.OPEN_DEPOSIT_INPUT -> ParticipantDetailModalState.DepositInput
                                ParticipantDetailActionType.OPEN_DELIVERY_CONFIRM -> ParticipantDetailModalState.DeliveryConfirm
                            }
                        },
                        type = ActionButtonType.SECONDARY_MAIN,
                        enabled = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 49.dp)
                            .padding(bottom = 14.dp),
                    )
                }
            }
        }
    }

    ParticipantDetailDialogs(
        modalState = modalState,
        recruiterInfo = RecruiterInfoForReview(
            name = detail.recruiterName,
            profileUrl = detail.recruiterProfileUrl,
            rating = detail.recruiterRating,
        ),
        onDismiss = { modalState = ParticipantDetailModalState.None },
        onDepositSubmit = onDepositSubmit,
        onDeliveryConfirm = onDeliveryConfirm,
        onNavigateToReview = {
            modalState = ParticipantDetailModalState.DeliveryReview(
                recruiterName = detail.recruiterName,
                recruiterProfileUrl = detail.recruiterProfileUrl,
                recruiterRating = detail.recruiterRating,
            )
        },
        onReviewSubmit = onReviewSubmit,
        onReviewSkip = onReviewSkip,
    )
}

class ParticipantDetailPreviewProvider : PreviewParameterProvider<ParticipantDetailUiModel> {
    override val values: Sequence<ParticipantDetailUiModel> = sequenceOf(
        DummyParticipantManageDetail.participantDetailWaitDeposit,
        DummyParticipantManageDetail.participantDetailCheckDeposit,
        DummyParticipantManageDetail.participantDetailDeliveryStart,
    )
}

@Preview(showBackground = true)
@Composable
private fun ParticipantDetailScreenPreview(
    @PreviewParameter(ParticipantDetailPreviewProvider::class) detail: ParticipantDetailUiModel,
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
