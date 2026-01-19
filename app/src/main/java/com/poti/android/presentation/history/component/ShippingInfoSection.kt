package com.poti.android.presentation.history.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.poti.android.domain.model.history.ParticipantShippingInfo

@Composable
fun ShippingInfoSection(
    info: ParticipantShippingInfo,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(id = R.string.history_shipping_info_title),
            style = PotiTheme.typography.body16sb,
            color = PotiTheme.colors.black,
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = stringResource(
                    R.string.history_shipping_info_format,
                    info.recipient,
                    info.zipcode,
                    info.address,
                    info.phone,
                ),
                style = PotiTheme.typography.body14m,
                color = PotiTheme.colors.black,
                lineHeight = PotiTheme.typography.body14m.fontSize * 1.5,
            )

            PotiItemOption(
                optionType = PotiItemOptionType.DELIVERY,
                sizeType = PotiItemOptionSize.SMALL,
                text = info.deliveryMethod,
            )
        }
    }
}
