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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.toMoneyString
import com.poti.android.core.common.util.screenHeightDp
import com.poti.android.core.designsystem.component.display.PotiItemOption
import com.poti.android.core.designsystem.component.display.PotiItemOptionSize
import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors

@Composable
fun HistoryParticipantOverview(
    memberList: String,
    userInfo: String,
    deliveryMethod: String,
    price: Int,
    participantStageType: StateLabelStage,
    participantStatusType: StateLabelStatus,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.white)
            .padding(
                horizontal = (16.dp),
                vertical = screenHeightDp(16.dp),
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = memberList,
                style = PotiTheme.typography.body16m,
                color = colors.black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(12.dp))

            HistoryStateLabel(
                sizeType = StateLabelSize.SMALL,
                stageType = participantStageType,
                statusType = participantStatusType,
            )
        }

        Text(
            text = userInfo,
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
                text = stringResource(
                    R.string.history_participant_detail_won_unit_format,
                    price.toMoneyString(),
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryParticipantOverviewPreview() {
    PotiTheme {
        val members = listOf("멤버명", "멤버명", "멤버명", "멤버명", "멤버명", "멤버명", "멤버명")
        val userName = "이포티"
        val zipcode = "01234"
        val address = "서울특별시 솝트구 다솝로 456"
        val phone = "010-2345-2345"

        HistoryParticipantOverview(
            memberList = members.joinToString(", "),
            userInfo = "$userName\n($zipcode) $address\n$phone",
            deliveryMethod = "준등기",
            price = 12800,
            participantStageType = StateLabelStage.DEPOSIT,
            participantStatusType = StateLabelStatus.CHECK,
            modifier = Modifier.width(375.dp),
        )
    }
}
