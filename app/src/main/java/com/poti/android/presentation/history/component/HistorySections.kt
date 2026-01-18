package com.poti.android.presentation.history.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.history.ArtistInfo
import com.poti.android.domain.model.history.ProgressInfo
import com.poti.android.presentation.history.DummyParticipantManageDetail.participantDetailWaitDeposit
import com.poti.android.presentation.history.mapper.toUiState

@Composable
fun PartyInfoSection(
    recruitId: Long,
    artistInfo: ArtistInfo,
    onDetailClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.history_recruit_number, recruitId),
            style = PotiTheme.typography.body14m,
            color = PotiTheme.colors.gray800,
            modifier = Modifier.padding(start = screenWidthDp(8.dp)),
        )

        val (partyStage, partyState) = artistInfo.partyState.toUiState()

        HistoryCardItem(
            sizeType = CardHistorySize.LARGE,
            imageUrl = artistInfo.imageUrl,
            artist = artistInfo.artist,
            title = artistInfo.title,
            participantStageType = partyStage,
            participantStatusType = partyState,
            onClick = { onDetailClick(recruitId) },
        )
    }
}

@Composable
fun ProgressStatusSection(
    progressInfo: ProgressInfo,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.history_progress_status_title),
            style = PotiTheme.typography.body16sb,
            color = PotiTheme.colors.black,
            modifier = Modifier
                .padding(bottom = 20.dp),
        )
        HistoryStateGuide(
            text = progressInfo.guideText,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(16.dp))

        Icon(
            imageVector = ImageVector.vectorResource(
                getStepIndicatorDrawable(progressInfo.step),
            ),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(horizontal = screenWidthDp(8.dp))
                .align(Alignment.CenterHorizontally),
        )
    }
}

@Composable
fun ParticipantManagementHeader(
    recruitId: Long,
    participantCount: Int,
    onHeaderClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = screenWidthDp(16.dp),
                end = screenWidthDp(4.dp),
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(id = R.string.history_participant_management_title, participantCount),
            style = PotiTheme.typography.body16sb,
            color = PotiTheme.colors.black,
        )
        IconButton(onClick = { onHeaderClick(recruitId) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right_lg),
                contentDescription = null,
                tint = PotiTheme.colors.gray700,
            )
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

@Preview(showBackground = true, name = "Party Info Section")
@Composable
private fun PartyInfoSectionPreview() {
    PotiTheme {
        PartyInfoSection(
            recruitId = 1L,
            artistInfo = participantDetailWaitDeposit.artistInfo,
            onDetailClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Progress Status Section (Step 0)")
@Composable
private fun ProgressStatusSectionPreview_Step0() {
    PotiTheme {
        ProgressStatusSection(
            progressInfo = participantDetailWaitDeposit.progressInfo,
            modifier = Modifier.size(360.dp, 206.dp),
        )
    }
}

@Preview(showBackground = true, name = "Progress Status Section (Step 2)")
@Composable
private fun ProgressStatusSectionPreview_Step2() {
    PotiTheme {
        ProgressStatusSection(
            progressInfo = participantDetailWaitDeposit.progressInfo,
            modifier = Modifier.size(360.dp, 206.dp),
        )
    }
}

@Preview(showBackground = true, name = "Participant Header (0)")
@Composable
private fun ParticipantManagementHeaderPreview_Empty() {
    PotiTheme {
        ParticipantManagementHeader(
            recruitId = 1L,
            participantCount = 0,
            onHeaderClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Participant Header (5)")
@Composable
private fun ParticipantManagementHeaderPreview_WithItems() {
    PotiTheme {
        ParticipantManagementHeader(
            recruitId = 1L,
            participantCount = 5,
            onHeaderClick = {},
        )
    }
}
