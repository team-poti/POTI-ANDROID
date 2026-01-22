package com.poti.android.presentation.history.recruiter.component

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
import com.poti.android.core.designsystem.theme.PotiTheme.typography
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.mapper.color
import com.poti.android.presentation.history.mapper.labelResId
import com.poti.android.presentation.history.mapper.statusColor
import com.poti.android.presentation.history.recruiter.model.ParticipantUiModel

@Composable
fun HistoryParticipantOverview(
    participant: ParticipantUiModel,
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
                text = participant.memberNamesString,
                style = typography.body16m,
                color = colors.black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(12.dp))

            Text(
                text = stringResource(participant.participantStatus.labelResId),
                style = typography.body14sb,
                color = participant.participantStatus.statusColor.color,
            )
        }

        Text(
            text = participant.shippingInfo,
            style = typography.body14m,
            color = colors.gray800,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PotiItemOption(
                optionType = PotiItemOptionType.DELIVERY,
                sizeType = PotiItemOptionSize.SMALL,
                text = participant.deliveryMethod,
            )
            PotiItemOption(
                optionType = PotiItemOptionType.PRICE,
                sizeType = PotiItemOptionSize.SMALL,
                text = stringResource(
                    R.string.history_participant_detail_won_unit_format,
                    participant.totalPrice.toMoneyString(),
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryParticipantOverviewPreview() {
    PotiTheme {
        HistoryParticipantOverview(
            participant = ParticipantUiModel(
                orderId = 1,
                userId = 1,
                memberNamesString = "레이, 이서",
                participantStatus = ParticipantStatusType.DELIVERED,
                shippingInfo = "이포티\n(01234) 서울특별시 솝트구 다솝로 456\n010-1234-5678",
                deliveryMethod = "준등기",
                totalPrice = 12800,
            ),
        )
    }
}
