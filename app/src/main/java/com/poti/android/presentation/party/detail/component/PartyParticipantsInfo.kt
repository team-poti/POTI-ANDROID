package com.poti.android.presentation.party.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.display.PotiEmptyStateInline
import com.poti.android.core.designsystem.component.display.PotiPrimaryTag
import com.poti.android.core.designsystem.component.display.PotiPrimaryTagColor
import com.poti.android.core.designsystem.component.display.PotiPrimaryTagSize
import com.poti.android.core.designsystem.component.display.PotiProfileSummary
import com.poti.android.core.designsystem.component.display.PotiProfileSummarySize
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.data.mock.UiMockData
import com.poti.android.domain.model.party.PartyDetail

@Composable
fun PartyParticipantsInfo(
    partyDetail: PartyDetail,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.party_detail_participants),
                style = PotiTheme.typography.body16sb,
                color = PotiTheme.colors.black,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = stringResource(R.string.party_detail_participants_count, partyDetail.currentCount, partyDetail.totalCount),
                style = PotiTheme.typography.body16sb,
                color = PotiTheme.colors.poti600,
            )
        }

        if (partyDetail.participants.isNotEmpty()) {
            partyDetail.participants.forEach { participant ->
                participant.selectedMembers.forEach { selectedMember ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        PotiProfileSummary(
                            profileImageUrl = participant.profileImage,
                            nickname = participant.nickname,
                            sizeType = PotiProfileSummarySize.LARGE,
                            rating = participant.rating.toString(),
                            modifier = Modifier.weight(1f),
                        )

                        PotiPrimaryTag(
                            text = selectedMember,
                            colorType = PotiPrimaryTagColor.GRAY,
                            sizeType = PotiPrimaryTagSize.LARGE,
                        )
                    }
                }
            }
        } else {
            PotiEmptyStateInline(
                text = stringResource(R.string.party_detail_participants_empty),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PartyParticipantsInfoPreview() {
    PotiTheme {
        PartyParticipantsInfo(
            partyDetail = UiMockData.partyDetail,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PartyParticipantsInfoPreviewEmpty() {
    PotiTheme {
        PartyParticipantsInfo(
            partyDetail = UiMockData.partyDetail.copy(participants = emptyList()),
        )
    }
}
