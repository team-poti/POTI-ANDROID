package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors
import com.poti.android.core.designsystem.theme.PotiTheme.typography
import androidx.compose.material3.Text
import androidx.compose.ui.text.TextStyle

enum class PotiPotiListOptionPriceSize {
    SMALL, LARGE
}
@Composable
fun PotiListOptionPrice(
    itemOptionType: PotiItemOptionType,
    itemOptionText: String,
    priceText: String,
    size: PotiPotiListOptionPriceSize,
    modifier: Modifier = Modifier,
) {
    val priceTextStyle: TextStyle = when (size) {
        PotiPotiListOptionPriceSize.SMALL -> typography.body14m
        PotiPotiListOptionPriceSize.LARGE -> typography.body16sb
    }
    val optionSize = when (size) {
        PotiPotiListOptionPriceSize.SMALL -> PotiItemOptionSize.SMALL
        PotiPotiListOptionPriceSize.LARGE -> PotiItemOptionSize.LARGE
    }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PotiItemOption(
            type = itemOptionType,
            size = optionSize,
            text = itemOptionText,
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = priceText,
            style = priceTextStyle,
            color = colors.black
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
                itemOptionText = "멤버명",
                priceText = "9,000원",
                size = PotiPotiListOptionPriceSize.SMALL
            )
            PotiListOptionPrice(
                itemOptionType = PotiItemOptionType.DELIVERY,
                itemOptionText = "배송방법",
                priceText = "12,000원",
                size = PotiPotiListOptionPriceSize.LARGE
            )
        }
    }
}
