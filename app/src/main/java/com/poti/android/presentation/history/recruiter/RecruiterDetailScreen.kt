package com.poti.android.presentation.history.recruiter

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.display.PotiEmptyStateInline
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.history.DummyParticipantManageDetail
import com.poti.android.presentation.history.component.HistoryParticipantOverview
import com.poti.android.presentation.history.component.ParticipantManagementHeader
import com.poti.android.presentation.history.component.PartyInfoSection
import com.poti.android.presentation.history.component.ProgressStatusSection
import com.poti.android.presentation.history.recruiter.model.RecruiterDetailUiEffect
import com.poti.android.presentation.history.recruiter.model.RecruiterDetailUiIntent
import com.poti.android.presentation.history.recruiter.model.RecruiterDetailUiModel

@Composable
fun RecruiterDetailRoute(
    onNavigateToMypageRecruit: () -> Unit,
    onNavigateToPartyDetail: (Long) -> Unit,
    onNavigateToParticipantManage: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecruiterViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                RecruiterDetailUiEffect.NavigateBack -> onNavigateToMypageRecruit()
                is RecruiterDetailUiEffect.NavigateToParticipantList -> onNavigateToParticipantManage(sideEffect.recruitId)
                is RecruiterDetailUiEffect.NavigateToPartyDetail -> onNavigateToPartyDetail(sideEffect.recruitId)
            }
        }
    }

    when (val state = uiState.recruiterDetailState) {
        is ApiState.Success -> {
            RecruiterDetailScreen(
                modifier = modifier,
                detail = state.data,
                onBackClick = { viewModel.processIntent(RecruiterDetailUiIntent.BackButtonClicked) },
                onDetailClick = { viewModel.processIntent(RecruiterDetailUiIntent.PartyCardClicked) },
                onParticipantManageDetailClick = { viewModel.processIntent(RecruiterDetailUiIntent.ParticipantSectionClicked) },
            )
        }
        else -> {}
    }
}

@Composable
private fun RecruiterDetailScreen(
    detail: RecruiterDetailUiModel,
    onBackClick: () -> Unit,
    onDetailClick: () -> Unit,
    onParticipantManageDetailClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            PotiHeaderPage(
                onNavigationClick = onBackClick,
                title = stringResource(id = detail.topBarTitleRes),
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                PartyInfoSection(
                    recruitId = detail.recruitId,
                    partyInfo = detail.artistInfo,
                    onDetailClick = onDetailClick,
                    modifier = Modifier.padding(
                        horizontal = screenWidthDp(8.dp),
                    ),
                )
            }

            item {
                ProgressStatusSection(
                    progressInfo = detail.progressInfo,
                    modifier = Modifier.padding(
                        horizontal = screenWidthDp(16.dp),
                    ),
                )
            }

            item {
                Spacer(Modifier.height(24.dp))
                PotiDivider(styleType = PotiDividerStyle.LARGE)
            }

            item {
                ParticipantManagementHeader(
                    participantCount = detail.participantCount,
                    onHeaderClick = onParticipantManageDetailClick,
                )
            }

            if (detail.participantCount == 0) {
                item {
                    PotiEmptyStateInline(
                        text = stringResource(id = R.string.history_no_participants),
                    )
                }
            } else {
                items(
                    items = detail.participantInfoList,
                    key = { it.userId },
                ) { participant ->
                    HistoryParticipantOverview(
                        memberList = participant.memberNames,
                        userInfo = participant.userInfo,
                        deliveryMethod = participant.deliveryMethod,
                        price = participant.deliveryPrice,
                        participantStageType = participant.stage,
                        participantStatusType = participant.status,
                    )
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
            detail = DummyParticipantManageDetail.recruiterRecruitStep,
            onBackClick = {},
            onDetailClick = {},
            onParticipantManageDetailClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Deposit Step")
@Composable
private fun RecruiterDetailScreenDepositPreview() {
    PotiTheme {
        RecruiterDetailScreen(
            detail = DummyParticipantManageDetail.recruiterDepositStep,
            onBackClick = {},
            onDetailClick = {},
            onParticipantManageDetailClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Delivery Done Step")
@Composable
private fun RecruiterDetailScreenDeliveryDonePreview() {
    PotiTheme {
        RecruiterDetailScreen(
            detail = DummyParticipantManageDetail.recruiterDeliveryDoneStep,
            onBackClick = {},
            onDetailClick = {},
            onParticipantManageDetailClick = {},
        )
    }
}
