package com.poti.android.presentation.history.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.history.model.PartySummaryUiModel

@Composable
fun PartyInfoSection(
    recruitId: Long,
    partyInfo: PartySummaryUiModel,
    onDetailClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.history_recruit_number, recruitId),
            style = PotiTheme.typography.body14m,
            color = PotiTheme.colors.gray800,
            modifier = Modifier.padding(start = screenWidthDp(8.dp)),
        )

        HistoryCardItem(
            sizeType = CardHistorySize.LARGE,
            imageUrl = partyInfo.imageUrl,
            artist = partyInfo.artist,
            title = partyInfo.title,
            stageType = partyInfo.partyStage,
            statusType = partyInfo.partyStatus,
            onClick = { onDetailClick() },
        )
    }
}

@Preview(showBackground = true, name = "Recruit Wait")
@Composable
private fun PartyInfoSectionRecruitWaitPreview() {
    PotiTheme {
        PartyInfoSection(
            recruitId = 1L,
            partyInfo = PartySummaryUiModel(
                imageUrl = "",
                artist = "NewJeans",
                title = "How Sweet 위버스 특전 분철",
                partyStage = StateLabelStage.RECRUIT,
                partyStatus = StateLabelStatus.WAIT,
            ),
            onDetailClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Deposit Wait")
@Composable
private fun PartyInfoSectionDepositWaitPreview() {
    PotiTheme {
        PartyInfoSection(
            recruitId = 1L,
            partyInfo = PartySummaryUiModel(
                imageUrl = "",
                artist = "IVE",
                title = "I've IVE",
                partyStage = StateLabelStage.DEPOSIT,
                partyStatus = StateLabelStatus.WAIT,
            ),
            onDetailClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Delivery Done")
@Composable
private fun PartyInfoSectionDeliveryDonePreview() {
    PotiTheme {
        PartyInfoSection(
            recruitId = 1L,
            partyInfo = PartySummaryUiModel(
                imageUrl = "",
                artist = "aespa",
                title = "Armageddon",
                partyStage = StateLabelStage.DELIVERY,
                partyStatus = StateLabelStatus.DONE,
            ),
            onDetailClick = {},
        )
    }
}
