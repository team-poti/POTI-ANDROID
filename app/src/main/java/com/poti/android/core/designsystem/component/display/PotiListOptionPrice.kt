package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors
import com.poti.android.core.designsystem.theme.PotiTheme.typography

enum class PotiListOptionPriceSize {
    SMALL,
    LARGE,
}

@Composable
fun PotiListOptionPriceSize.textStyle() =
    when (this) {
        PotiListOptionPriceSize.SMALL -> typography.body14m
        PotiListOptionPriceSize.LARGE -> typography.body16sb
    }

@Composable
fun PotiListOptionPriceSize.optionSize() =
    when (this) {
        PotiListOptionPriceSize.SMALL -> PotiItemOptionSize.SMALL
        PotiListOptionPriceSize.LARGE -> PotiItemOptionSize.LARGE
    }

@Composable
fun PotiListOptionPrice(
    itemOptionType: PotiItemOptionType,
    itemOptionText: String,
    priceText: String,
    sizeType: PotiListOptionPriceSize,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PotiItemOption(
            optionType = itemOptionType,
            sizeType = sizeType.optionSize(),
            text = itemOptionText,
            modifier = Modifier.weight(1f),
        )

        Text(
            text = priceText,
            style = sizeType.textStyle(),
            color = colors.black,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PotiListOptionPricePreview() {
    PotiTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PotiListOptionPrice(
                itemOptionType = PotiItemOptionType.MEMBER,
                itemOptionText = "멤버명멤버명멤버명멤버명멤버명멤버명멤버명멤버명멤버명멤버명",
                priceText = "9,000원",
                sizeType = PotiListOptionPriceSize.SMALL,
            )
            PotiListOptionPrice(
                itemOptionType = PotiItemOptionType.DELIVERY,
                itemOptionText = "배송방법",
                priceText = "12,000원",
                sizeType = PotiListOptionPriceSize.LARGE,
            )
        }
    }
}
