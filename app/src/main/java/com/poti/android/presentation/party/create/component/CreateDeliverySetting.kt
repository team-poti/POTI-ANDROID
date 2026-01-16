package com.poti.android.presentation.party.create.component

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
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.delivery.DeliveryOption

@Composable
fun CreateDeliverySetting(
    deliveryOptions: List<DeliveryOption>,
    selectedOptionIds: Set<Long>,
    onDeliveryOptionClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = screenWidthDp(16.dp),
                vertical = 24.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Text(
            text = stringResource(R.string.create_label_delivery),
            color = PotiTheme.colors.black,
            style = PotiTheme.typography.title18sb,
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            deliveryOptions.forEach { option ->
                EditOptionPrice(
                    option = option.name,
                    value = option.price.toString(),
                    onValueChanged = {},
                    isChecked = option.deliveryId in selectedOptionIds,
                    onCheckboxClick = { onDeliveryOptionClick(option.deliveryId) },
                    enabled = false,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CreateDeliverySettingPreview() {
    val deliveryOptions = listOf(
        DeliveryOption(deliveryId = 1, name = "일반택배", price = 4000),
        DeliveryOption(deliveryId = 2, name = "준등기", price = 1800),
    )

    val selectedOptionIds = setOf(1.toLong())

    PotiTheme {
        CreateDeliverySetting(
            deliveryOptions = deliveryOptions,
            selectedOptionIds = selectedOptionIds,
            onDeliveryOptionClick = {},
        )
    }
}
