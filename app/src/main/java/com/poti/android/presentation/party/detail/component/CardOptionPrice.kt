package com.poti.android.presentation.party.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.component.button.PotiDeleteButton
import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.core.designsystem.component.display.PotiListOptionPrice
import com.poti.android.core.designsystem.component.display.PotiListOptionPriceSize
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun CardOptionPrice(
    optionType: PotiItemOptionType,
    text: String,
    price: String,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = {},
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (enabled) PotiTheme.colors.gray100 else PotiTheme.colors.gray100.copy(alpha = 0.2f))
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PotiListOptionPrice(
            itemOptionType = optionType,
            itemOptionText = text,
            priceText = price,
            sizeType = PotiListOptionPriceSize.LARGE,
            modifier = Modifier
                .weight(1f)
                .heightIn(48.dp),
        )

        if (optionType == PotiItemOptionType.MEMBER) {
            PotiDeleteButton(
                onClick = onDeleteClick,
            )
        } else {
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}
