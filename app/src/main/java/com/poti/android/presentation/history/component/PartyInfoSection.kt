package com.poti.android.presentation.history.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.history.PartySummary
import com.poti.android.domain.type.PartyStatusType
import com.poti.android.presentation.history.mapper.color
import com.poti.android.presentation.history.mapper.labelResId
import com.poti.android.presentation.history.mapper.statusColor

@Composable
fun PartyInfoSection(
    orderNumber: String,
    partySummary: PartySummary,
    onDetailClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = orderNumber,
            style = PotiTheme.typography.body14m,
            color = PotiTheme.colors.gray800,
            modifier = Modifier.padding(start = 8.dp, top = 12.dp, bottom = 4.dp),
        )

        HistoryCardItem(
            sizeType = CardHistorySize.LARGE,
            imageUrl = partySummary.imageUrl,
            artist = partySummary.artist,
            title = partySummary.title,
            statusTextId = partySummary.partyStatus.labelResId,
            statusColor = partySummary.partyStatus.statusColor.color,
            onClick = onDetailClick,
        )
    }
}

@Preview(showBackground = true, name = "Recruit Wait")
@Composable
private fun PartyInfoSectionRecruitWaitPreview() {
    PotiTheme {
        PartyInfoSection(
            orderNumber = "모집번호 poti-01",
            partySummary = PartySummary(
                imageUrl = "",
                artist = "ive(아이브)",
                title = "러브다이브 위드뮤",
                partyStatus = PartyStatusType.RECRUITING,
                statusMessage = "상태 메시지를 입력하세요",
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
            orderNumber = "모집번호 poti-01",
            partySummary = PartySummary(
                imageUrl = "",
                artist = "ive(아이브)",
                title = "러브다이브 위드뮤",
                partyStatus = PartyStatusType.CLOSED,
                statusMessage = "입금을 기다리는 중이에요",
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
            orderNumber = "모집번호 poti-01",
            partySummary = PartySummary(
                imageUrl = "",
                artist = "ive(아이브)",
                title = "러브다이브 위드뮤",
                partyStatus = PartyStatusType.DELIVERED,
                statusMessage = "거래가 종료되었어요",
            ),
            onDetailClick = {},
        )
    }
}
