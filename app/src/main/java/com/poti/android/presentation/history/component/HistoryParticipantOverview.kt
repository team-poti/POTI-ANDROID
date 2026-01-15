package com.poti.android.presentation.history.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.component.display.PotiItemOption
import com.poti.android.core.designsystem.component.display.PotiItemOptionSize
import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors

@Composable
fun HistoryParticipantOverview(
    modifier: Modifier = Modifier,
    memberList: List<String>,
    userName: String,
    address: String,
    phone: String,
    deliveryMethod: String,
    price: Int,
    participantStageType: ParticipantStateLabelStage,
    participantStatusType: ParticipantStateLabelStatus,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.white)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = memberList.joinToString(", "),
                style = PotiTheme.typography.body16m,
                color = colors.black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(12.dp))

            HistoryParticipantStateLabel(
                sizeType = ParticipantStateLabelSize.SMALL,
                stageType = participantStageType,
                statusType = participantStatusType,
            )
        }

        Text(
            text = "$userName\n$address\n$phone",
            style = PotiTheme.typography.body14m,
            color = colors.gray800,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PotiItemOption(
                optionType = PotiItemOptionType.DELIVERY,
                sizeType = PotiItemOptionSize.SMALL,
                text = deliveryMethod,
            )
            PotiItemOption(
                optionType = PotiItemOptionType.PRICE,
                sizeType = PotiItemOptionSize.SMALL,
                text = priceText(price),
            )
        }
    }
}

// TODO: [천민재] 임시 util 함수
private fun priceText(price: Int) = String.format("%,d원", price)

@Preview(showBackground = true)
@Composable
private fun HistoryParticipantOverviewPreview() {
    PotiTheme {
        HistoryParticipantOverview(
            memberList =
                listOf("멤버명", "멤버명", "멤버명", "멤버명", "멤버명", "멤버명", "멤버명"),
            address = "(01234) 서울특별시 솝트구 다솝로 456",
            userName = "이포티",
            phone = "010-2345-2345",
            deliveryMethod = "준등기",
            price = 12800,
            participantStageType = ParticipantStateLabelStage.DEPOSIT,
            participantStatusType = ParticipantStateLabelStatus.CHECK,
            modifier = Modifier.width(375.dp),
        )
    }
}
