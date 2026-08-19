package com.poti.android.presentation.party.create.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.display.PotiErrorMessage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.party.create.model.DeliveryOptionUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun CreateDeliverySetting(
    deliveryOptions: ImmutableList<DeliveryOptionUiModel>,
    onDeliveryClick: (DeliveryOptionUiModel) -> Unit,
    onPriceChange: (DeliveryOptionUiModel) -> Unit,
    errorMessage: String,
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
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.create_label_delivery),
                color = PotiTheme.colors.black,
                style = PotiTheme.typography.title18sb,
            )

            if (errorMessage.isNotEmpty()) {
                PotiErrorMessage(errorMessage)
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            deliveryOptions.forEach { option ->
                EditOptionPrice(
                    option = option.name,
                    value = option.priceInput,
                    onValueChanged = { newPrice ->
                        onPriceChange(option.copy(priceInput = newPrice))
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
        DeliveryOptionUiModel(deliveryId = 1, name = "일반택배", priceInput = "4000", isSelected = true),
        DeliveryOptionUiModel(deliveryId = 2, name = "준등기", priceInput = "1800", isSelected = false),
    )

    PotiTheme {
        CreateDeliverySetting(
            deliveryOptions = deliveryOptions,
            onDeliveryClick = {},
            onPriceChange = {},
            errorMessage = "",
        )
    }
}
