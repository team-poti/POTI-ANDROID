package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.Poti600
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors
import com.poti.android.core.designsystem.theme.PotiTheme.typography

enum class PotiPrimaryTagSize {
    SMALL,
    LARGE,
}

enum class PotiPrimaryTagColor {
    GRAY,
    WHITE,
}

val PotiPrimaryTagSize.textStyle: TextStyle
    @Composable get() = when (this) {
        PotiPrimaryTagSize.SMALL -> typography.caption12m
        PotiPrimaryTagSize.LARGE -> typography.body14m
    }

val PotiPrimaryTagColor.color: Color
    @Composable get() = when (this) {
        PotiPrimaryTagColor.GRAY -> colors.gray100
        PotiPrimaryTagColor.WHITE -> colors.white
    }

@Composable
fun PotiPrimaryTag(
    text: String,
    colorType: PotiPrimaryTagColor,
    sizeType: PotiPrimaryTagSize,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = colorType.color
    val textStyle = sizeType.textStyle

    Text(
        text = text,
        style = textStyle,
        color = Poti600,
        modifier = modifier
            .clip(RoundedCornerShape(99.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    )
}

@Preview
@Composable
private fun PotiPrimaryTagPreview() {
    PotiTheme {
        Column {
            PotiPrimaryTag(text = "팟 2개", sizeType = PotiPrimaryTagSize.SMALL, colorType = PotiPrimaryTagColor.WHITE)
            PotiPrimaryTag(text = "팟 3개", sizeType = PotiPrimaryTagSize.SMALL, colorType = PotiPrimaryTagColor.GRAY)
            PotiPrimaryTag(text = "팟 4개", sizeType = PotiPrimaryTagSize.LARGE, colorType = PotiPrimaryTagColor.WHITE)
            PotiPrimaryTag(text = "팟 N개", sizeType = PotiPrimaryTagSize.LARGE, colorType = PotiPrimaryTagColor.GRAY)
        }
    }
}
