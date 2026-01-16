package com.poti.android.presentation.history.recruiter

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.display.PotiEmptyStateInline
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.history.ParticipantManageDetail
import com.poti.android.presentation.history.DummyParticipantManageDetail
import com.poti.android.presentation.history.component.CardHistorySize
import com.poti.android.presentation.history.component.HistoryCardItem
import com.poti.android.presentation.history.component.HistoryParticipantOverview
import com.poti.android.presentation.history.component.HistoryStateGuide
import com.poti.android.presentation.history.mapper.toUiState

@Composable
fun ParticipantManageRoute(
    onBackClick: () -> Unit,
    onDetailClick: (Int) -> Unit,
    onParticipantManageDetailClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ParticipantManageScreen(
        modifier = modifier,
        // TODO: [천민재] viewModel 만들고 연결
        detail = DummyParticipantManageDetail.deliveryDoneStep,
        onBackClick = onBackClick,
        onDetailClick = onDetailClick,
        onParticipantManageDetailClick = onParticipantManageDetailClick,
    )
}

@Composable
private fun ParticipantManageScreen(
    detail: ParticipantManageDetail,
    onBackClick: () -> Unit,
    onDetailClick: (Int) -> Unit,
    onParticipantManageDetailClick: (Int) -> Unit,
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
                .fillMaxSize()
                .padding(paddingValues)
                .background(PotiTheme.colors.white),
        ) {
            item {
                Column {
                    Text(
                        text = stringResource(id = R.string.history_recruit_number,
                            detail.recruitId),
                        style = PotiTheme.typography.body14m,
                        color = PotiTheme.colors.gray800,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    val artist = detail.artistInfo
                    val (partyStage, partyState) = artist.partyState.toUiState()

                    HistoryCardItem(
                        sizeType = CardHistorySize.LARGE,
                        imageUrl = artist.imageUrl,
                        artist = artist.artist,
                        title = artist.title,
                        participantStageType = partyStage,
                        participantStatusType = partyState,
                        onClick = { onDetailClick(detail.recruitId) },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }

            item {
                Text(
                    text = stringResource(id = R.string.history_progress_status_title),
                    style = PotiTheme.typography.body16sb,
                    color = PotiTheme.colors.black,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = 20.dp)
                        .padding(start = 16.dp)
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HistoryStateGuide(
                        text = detail.progressInfo.guideText,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            getStepIndicatorDrawable(
                                detail.progressInfo.step)
                        ),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .width(screenWidthDp(312.dp))
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }

            item {
                PotiDivider(
                    styleType = PotiDividerStyle.LARGE,
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.history_participant_management_title,
                            detail.participantCount),
                        style = PotiTheme.typography.body16sb,
                        color = PotiTheme.colors.black
                    )
                    IconButton(onClick = { onParticipantManageDetailClick(detail.recruitId) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_right_lg),
                            contentDescription = "More",
                            tint = PotiTheme.colors.gray700
                        )
                    }
                }
            }

            if(detail.participantCount == 0) {
                item {
                    PotiEmptyStateInline(
                        text = stringResource(id = R.string.history_no_participants)
                    )
                }
            } else {
                val participants = detail.participantInfoList

                items(
                    items = participants,
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
private fun getStepIndicatorDrawable(step: Int): Int {
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
private fun ParticipantManageScreenPreview() {
    PotiTheme {
        ParticipantManageRoute(
            onBackClick = {},
            onDetailClick = {},
            onParticipantManageDetailClick = {},
        )
    }
}
