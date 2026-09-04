package com.poti.android.presentation.history.recruiter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.extension.onSuccess
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.designsystem.component.button.PotiTextButton
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.display.PotiEmptyStateInline
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.data.mock.UiMockData
import com.poti.android.domain.type.PartyStatusType
import com.poti.android.presentation.history.component.HistoryDetailContentHeader
import com.poti.android.presentation.history.component.PartyInfoSection
import com.poti.android.presentation.history.component.ProgressStatusSection
import com.poti.android.presentation.history.recruiter.component.HistoryParticipantOverview
import com.poti.android.presentation.history.recruiter.model.RecruiterDetailUiEffect
import com.poti.android.presentation.history.recruiter.model.RecruiterDetailUiIntent
import com.poti.android.presentation.history.recruiter.model.RecruiterDetailUiModel
import com.poti.android.presentation.history.recruiter.model.toUiModel

@Composable
fun RecruiterDetailRoute(
    onPopBackStack: () -> Unit,
    onNavigateToPartyDetail: (Long) -> Unit,
    onNavigateToParticipantManage: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecruiterViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.processIntent(RecruiterDetailUiIntent.OnResume)
    }

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            RecruiterDetailUiEffect.NavigateBack -> onPopBackStack()
            is RecruiterDetailUiEffect.NavigateToParticipantList -> onNavigateToParticipantManage(effect.recruitId)
            is RecruiterDetailUiEffect.NavigateToPartyDetail -> onNavigateToPartyDetail(effect.recruitId)
        }
    }

    Box(modifier = modifier) {
        uiState.recruiterDetailState.onSuccess { recruiterDetail ->
            RecruiterDetailScreen(
                recruiterDetail = recruiterDetail,
                onBackClick = { viewModel.processIntent(RecruiterDetailUiIntent.BackButtonClicked) },
                onDetailClick = { viewModel.processIntent(RecruiterDetailUiIntent.PartyCardClicked) },
                onParticipantManageDetailClick = { viewModel.processIntent(RecruiterDetailUiIntent.ParticipantSectionClicked) },
                onDeleteClick = { viewModel.processIntent(RecruiterDetailUiIntent.DeleteButtonClicked) },
            )
        }
    }
}

@Composable
private fun RecruiterDetailScreen(
    recruiterDetail: RecruiterDetailUiModel,
    onBackClick: () -> Unit,
    onDetailClick: () -> Unit,
    onParticipantManageDetailClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDeletable = recruiterDetail.partySummary.partyStatus == PartyStatusType.RECRUITING &&
        recruiterDetail.participantCount == 0

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        PotiHeaderPage(
            onNavigationClick = onBackClick,
            title = stringResource(id = R.string.history_ongoing_title),
            trailingContent = if (isDeletable) {
                {
                    PotiTextButton(
                        text = stringResource(id = R.string.history_recruiter_delete),
                        onClick = onDeleteClick,
                    )
                }
            } else {
                null
            },
        )

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 50.dp),
        ) {
            item {
                PartyInfoSection(
                    orderNumber = recruiterDetail.orderNumber,
                    partySummary = recruiterDetail.partySummary,
                    onDetailClick = onDetailClick,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }

            item {
                ProgressStatusSection(
                    progressStatus = recruiterDetail.partySummary.partyStatus,
                    statusMessage = recruiterDetail.partySummary.statusMessage,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }

            item {
                PotiDivider(
                    styleType = PotiDividerStyle.LARGE,
                    modifier = Modifier.padding(top = 24.dp),
                )
            }

            item {
                HistoryDetailContentHeader(
                    text = stringResource(id = R.string.history_recruiter_participant_management_title, recruiterDetail.participantCount),
                    onHeaderClick = onParticipantManageDetailClick,
                )
            }

            if (recruiterDetail.participantCount == 0) {
                item {
                    PotiEmptyStateInline(
                        text = stringResource(id = R.string.history_no_participants),
                    )
                }
            } else {
                itemsIndexed(
                    items = recruiterDetail.participants,
                    key = { _, participant -> participant.orderId },
                ) { index, participant ->
                    Column {
                        HistoryParticipantOverview(
                            participant = participant,
                        )
                        if (index < recruiterDetail.participants.lastIndex) {
                            PotiDivider(
                                styleType = PotiDividerStyle.SMALL,
                                modifier = Modifier.padding(8.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Recruit Step")
@Composable
private fun RecruiterDetailScreenRecruitPreview() {
    PotiTheme {
        RecruiterDetailScreen(
            recruiterDetail = UiMockData.recruiterDetail.toUiModel(),
            onBackClick = {},
            onDetailClick = {},
            onParticipantManageDetailClick = {},
            onDeleteClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Deposit Step")
@Composable
private fun RecruiterDetailScreenDepositPreview() {
    PotiTheme {
        RecruiterDetailScreen(
            recruiterDetail = UiMockData.recruiterDetail.toUiModel(),
            onBackClick = {},
            onDetailClick = {},
            onParticipantManageDetailClick = {},
            onDeleteClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Delivery Done Step")
@Composable
private fun RecruiterDetailScreenDeliveryDonePreview() {
    PotiTheme {
        RecruiterDetailScreen(
            recruiterDetail = UiMockData.recruiterDetail.toUiModel(),
            onBackClick = {},
            onDetailClick = {},
            onParticipantManageDetailClick = {},
            onDeleteClick = {},
        )
    }
}
