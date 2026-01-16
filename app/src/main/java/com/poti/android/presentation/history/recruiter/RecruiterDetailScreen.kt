package com.poti.android.presentation.history.recruiter

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
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
import com.poti.android.domain.model.history.RecruiterDetail
import com.poti.android.presentation.history.DummyParticipantManageDetail
import com.poti.android.presentation.history.component.HistoryParticipantOverview
import com.poti.android.presentation.history.component.ParticipantManagementHeader
import com.poti.android.presentation.history.component.PartyInfoSection
import com.poti.android.presentation.history.component.ProgressStatusSection
import com.poti.android.presentation.history.mapper.toUiState
import com.poti.android.presentation.history.model.RecruiterDetailUiEffect

@Composable
fun RecruiterDetailRoute(
    onBackClick: () -> Unit,
    onDetailClick: (Long) -> Unit,
    onParticipantManageDetailClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecruiterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                RecruiterDetailUiEffect.NavigateBack -> onBackClick()
                is RecruiterDetailUiEffect.NavigateToParticipantList -> onParticipantManageDetailClick(sideEffect.partyId)
                is RecruiterDetailUiEffect.NavigateToPartyDetail -> onDetailClick(sideEffect.partyId)
            }
        }
    }

    when(val state = uiState.recruiterDetail) {
        is ApiState.Success -> {
            RecruiterDetailScreen(
                modifier = modifier,
                detail = state.data,
                onBackClick = onBackClick,
                onDetailClick = onDetailClick,
                onParticipantManageDetailClick = onParticipantManageDetailClick,
            )
        }
        else -> {}
    }
}

@Composable
private fun RecruiterDetailScreen(
    detail: RecruiterDetail,
    onBackClick: () -> Unit,
    onDetailClick: (Long) -> Unit,
    onParticipantManageDetailClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            PotiHeaderPage(
                onNavigationClick = onBackClick,
                title = stringResource(id = R.string.history_ongoing_title)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                PartyInfoSection(
                    partyId = detail.partyId,
                    artistInfo = detail.artistInfo,
                    onDetailClick = onDetailClick,
                    modifier = Modifier.padding(
                        horizontal = screenWidthDp(8.dp))
                )
            }

            item {
                ProgressStatusSection(
                    progressInfo = detail.progressInfo,
                    modifier = Modifier.padding(
                        horizontal = screenWidthDp(16.dp))
                )
            }

            item {
                Spacer(Modifier.height(24.dp))
                PotiDivider(styleType = PotiDividerStyle.LARGE)
            }

            item {
                ParticipantManagementHeader(
                    partyId = detail.partyId,
                    participantCount = detail.participantCount,
                    onHeaderClick = onParticipantManageDetailClick
                )
            }

            if (detail.participantCount == 0) {
                item {
                    PotiEmptyStateInline(
                        text = stringResource(id = R.string.history_no_participants)
                    )
                }
            } else {
                items(
                    items = detail.participantInfoList,
                    key = { it.userId }
                ) { participant ->
                    val (stage, status) = participant.participantState.toUiState()

                    HistoryParticipantOverview(
                        memberList = participant.memberNames,
                        userInfo = participant.userInfo,
                        deliveryMethod = participant.deliveryMethod,
                        price = participant.deliveryPrice,
                        participantStageType = stage,
                        participantStatusType = status,
                    )
                }
            }
        }
    }
}

@DrawableRes
fun getStepIndicatorDrawable(step: Int): Int {
    return when (step) {
        0 -> R.drawable.ic_history_step_indicator_0
        1 -> R.drawable.ic_history_step_indicator_1
        2 -> R.drawable.ic_history_step_indicator_2
        3 -> R.drawable.ic_history_step_indicator_3
        4 -> R.drawable.ic_history_step_indicator_4
        else -> R.drawable.ic_history_step_indicator_0
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruiterDetailScreenPreview() {
    PotiTheme {
        RecruiterDetailScreen(
            detail = DummyParticipantManageDetail.deliveryDoneStep,
            onBackClick = {},
            onDetailClick = {},
            onParticipantManageDetailClick = {},
        )
    }
}
