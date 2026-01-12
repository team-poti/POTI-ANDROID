package com.poti.android.core.designsystem.component.display

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors
import com.poti.android.core.designsystem.theme.PotiTheme.typography

enum class PotiItemOptionType(val iconResId: Int) {
    MEMBER(R.drawable.ic_member),
    DELIVERY(R.drawable.ic_delivery),
    PRICE(R.drawable.ic_price),
}

enum class PotiItemOptionSize(val size: Dp) {
    LARGE(24.dp),
    SMALL(21.dp),
}

val PotiItemOptionSize.textStyle: TextStyle
    @Composable get() = when (this) {
        PotiItemOptionSize.LARGE -> typography.body16m
        PotiItemOptionSize.SMALL -> typography.body14m
    }

@Composable
fun PotiItemOption(
    optionType: PotiItemOptionType,
    sizeType: PotiItemOptionSize,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = optionType.iconResId),
            contentDescription = null,
            modifier = Modifier.size(sizeType.size),
            tint = colors.gray800,
        )
        Text(
            text = text,
            style = sizeType.textStyle,
            color = colors.gray800,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PotiItemOptionPreview() {
    PotiTheme {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            PotiItemOption(optionType = PotiItemOptionType.MEMBER, sizeType = PotiItemOptionSize.LARGE, text = "멤버명")
            PotiItemOption(optionType = PotiItemOptionType.DELIVERY, sizeType = PotiItemOptionSize.LARGE, text = "배송방법")
            PotiItemOption(optionType = PotiItemOptionType.PRICE, sizeType = PotiItemOptionSize.LARGE, text = "9,000원")

            PotiItemOption(optionType = PotiItemOptionType.MEMBER, sizeType = PotiItemOptionSize.SMALL, text = "멤버명")
            PotiItemOption(optionType = PotiItemOptionType.DELIVERY, sizeType = PotiItemOptionSize.SMALL, text = "배송방법")
            PotiItemOption(optionType = PotiItemOptionType.PRICE, sizeType = PotiItemOptionSize.SMALL, text = "9,000원")
        }
    }
}
