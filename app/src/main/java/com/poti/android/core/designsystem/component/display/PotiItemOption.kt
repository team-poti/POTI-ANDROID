package com.poti.android.core.designsystem.component.display

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors
import com.poti.android.core.designsystem.theme.PotiTheme.typography

enum class PotiItemOptionType(val iconResId: Int) {
    MEMBER(R.drawable.ic_member),
    DELIVERY(R.drawable.ic_delivery),
    PRICE(R.drawable.ic_price);
}

enum class PotiItemOptionSize {
    LARGE,
    SMALL
}
val PotiItemOptionSize.textStyle: TextStyle
    @Composable get() = when (this) {
        PotiItemOptionSize.LARGE -> typography.body16m
        PotiItemOptionSize.SMALL -> typography.body14m
    }

@Composable
fun PotiItemOption(
    type: PotiItemOptionType,
    size: PotiItemOptionSize,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = type.iconResId),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = colors.gray800
        )
        Text(
            text = text,
            style = size.textStyle,
            color = colors.gray800
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PotiItemOptionPreview() {
    PotiTheme {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            PotiItemOption(type = PotiItemOptionType.MEMBER, size = PotiItemOptionSize.LARGE, text = "멤버명")
            PotiItemOption(type = PotiItemOptionType.DELIVERY, size = PotiItemOptionSize.LARGE, text = "배송방법")
            PotiItemOption(type = PotiItemOptionType.PRICE, size = PotiItemOptionSize.LARGE, text = "9,000원")

            PotiItemOption(type = PotiItemOptionType.MEMBER, size = PotiItemOptionSize.SMALL, text = "멤버명")
            PotiItemOption(type = PotiItemOptionType.DELIVERY, size = PotiItemOptionSize.SMALL, text = "배송방법")
            PotiItemOption(type = PotiItemOptionType.PRICE, size = PotiItemOptionSize.SMALL, text = "9,000원")
        }
    }
}
