package com.poti.android.presentation.history.participant.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.display.PotiItemOption
import com.poti.android.core.designsystem.component.display.PotiItemOptionSize
import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.mapper.color
import com.poti.android.presentation.history.mapper.labelResId
import com.poti.android.presentation.history.mapper.statusColor
import com.poti.android.presentation.history.participant.model.ParticipantShippingUiModel

@Composable
fun DeliveryStatusContent(
    shippingInfo: ParticipantShippingUiModel,
    participantStatusType: ParticipantStatusType,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = shippingInfo.addressInfo,
            style = PotiTheme.typography.body14m,
            color = PotiTheme.colors.black,
        )

        PotiItemOption(
            optionType = PotiItemOptionType.DELIVERY,
            sizeType = PotiItemOptionSize.SMALL,
            text = stringResource(R.string.history_delivery_method),
        )

        when (participantStatusType) {
            ParticipantStatusType.READY, ParticipantStatusType.DELIVERED -> {
                Text(
                    text = stringResource(participantStatusType.labelResId),
                    color = participantStatusType.statusColor.color,
                    style = PotiTheme.typography.body16sb,
                    modifier = Modifier.align(Alignment.End),
                )
            }
            ParticipantStatusType.SHIPPED -> {
                HistoryCalloutInfo(
                    text = shippingInfo.deliveryTrackingInfo,
                    copyable = true,
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                )
                Text(
                    text = stringResource(participantStatusType.labelResId),
                    color = participantStatusType.statusColor.color,
                    style = PotiTheme.typography.body16sb,
                    modifier = Modifier.align(Alignment.End),
                )
            }
            else -> {}
        }
    }
}
