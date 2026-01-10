package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.Gray100
import com.poti.android.core.designsystem.theme.Poti600
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.typography
import com.poti.android.core.designsystem.theme.White

enum class PotiChipSize {
    SMALL,
    LARGE;

    @Composable
    fun toTextStyle() = when (this) {
        SMALL -> typography.caption12m
        LARGE -> typography.body14m
    }
}

enum class PotiChipColor {
    GRAY,
    WHITE;

    fun toColor() = when (this) {
        GRAY -> Gray100
        WHITE -> White
    }
}

@Composable
fun PotiChip(
    text: String,
    color: PotiChipColor,
    size: PotiChipSize,
    modifier: Modifier = Modifier
) {
    val backgroundColor = color.toColor()
    val textStyle = size.toTextStyle()

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(99.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = textStyle,
            color = Poti600
        )
    }
}

@Preview
@Composable
private fun PotiChipPreview() {
    PotiTheme {
        Column {
            PotiChip(text = "팟 2개", size = PotiChipSize.SMALL, color = PotiChipColor.WHITE)
            PotiChip(text = "팟 3개", size = PotiChipSize.SMALL, color = PotiChipColor.GRAY)
            PotiChip(text = "팟 4개", size = PotiChipSize.LARGE, color = PotiChipColor.WHITE)
            PotiChip(text = "팟 N개", size = PotiChipSize.LARGE, color = PotiChipColor.GRAY)
        }
    }
}
