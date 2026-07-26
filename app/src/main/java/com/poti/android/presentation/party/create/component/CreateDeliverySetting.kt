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
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.party.create.model.DeliveryOptionUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun CreateDeliverySetting(
    deliveryOptions: ImmutableList<DeliveryOptionUiModel>,
    onDeliveryClick: (DeliveryOptionUiModel) -> Unit,
    onPriceChange: (DeliveryOptionUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
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
                    onValueChanged = { newPrice ->
                        onPriceChange(option.copy(price = newPrice.toIntOrNull() ?: 0))
                    },
                    isChecked = option.isSelected,
                    onCheckboxClick = { onDeliveryClick(option) },
                    enabled = option.isSelected,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CreateDeliverySettingPreview() {
    val deliveryOptions = persistentListOf(
        DeliveryOptionUiModel(deliveryId = 1, name = "일반택배", price = 4000, isSelected = true),
        DeliveryOptionUiModel(deliveryId = 2, name = "준등기", price = 1800, isSelected = false),
    )

    PotiTheme {
        CreateDeliverySetting(
            deliveryOptions = deliveryOptions,
            onDeliveryClick = {},
            onPriceChange = {},
        )
    }
}