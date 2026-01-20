package com.poti.android.presentation.history.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.display.PotiItemOption
import com.poti.android.core.designsystem.component.display.PotiItemOptionSize
import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.history.participant.model.ShippingInfoUiModel

@Composable
fun ShippingInfoSection(
    info: ShippingInfoUiModel,
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
                text ="${info.recipient}\n(${info.zipcode}) ${info.address}${info.phone}",
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

@Preview(showBackground = true)
@Composable
private fun ShippingInfoSectionPreview() {
    PotiTheme {
        ShippingInfoSection(
            info = ShippingInfoUiModel(
                recipient = "김포티",
                zipcode = "06000",
                address = "서울특별시 강남구 테헤란로 123 포티타워 101호",
                phone = "010-1234-5678",
                deliveryMethod = "GS25 반값택배",
                trackingNumber = null,
            ),
        )
    }
}
