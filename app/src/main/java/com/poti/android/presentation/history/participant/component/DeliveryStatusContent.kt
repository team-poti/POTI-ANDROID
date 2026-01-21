package com.poti.android.presentation.history.participant.component

import ParticipantShippingUiModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.display.PotiItemOption
import com.poti.android.core.designsystem.component.display.PotiItemOptionSize
import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.type.ParticipantStatusType

@Composable
fun DeliveryStatusContent(
    shippingInfo: ParticipantShippingUiModel,
    participantStatusType: ParticipantStatusType,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier.padding(bottom = 24.dp),
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
            ParticipantStatusType.RECRUITING -> {}
            ParticipantStatusType.WAIT_PAY -> {}
            ParticipantStatusType.WAIT_PAY_CHECK -> {}
            ParticipantStatusType.PAID -> {}
            ParticipantStatusType.READY -> {}
            ParticipantStatusType.SHIPPED -> {}
            ParticipantStatusType.DELIVERED -> {}
        }
    }
}
