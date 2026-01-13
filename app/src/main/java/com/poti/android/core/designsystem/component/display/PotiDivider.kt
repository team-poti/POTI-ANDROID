package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.PotiTheme

enum class PotiDividerStyle(val height: Dp) {
    SMALL(height = 1.dp),
    LARGE(height = 8.dp),
}

val PotiDividerStyle.color: Color
    @Composable get() = when (this) {
        PotiDividerStyle.SMALL -> PotiTheme.colors.gray300
        PotiDividerStyle.LARGE -> PotiTheme.colors.gray100
    }

@Composable
fun PotiDivider(
    styleType: PotiDividerStyle,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(styleType.height)
            .background(styleType.color),
    )
}

@Preview(showBackground = true)
@Composable
private fun PotiDividerPreview() {
    PotiTheme {
        Column {
            PotiDivider(styleType = PotiDividerStyle.SMALL)

            Spacer(Modifier.height(20.dp))

            PotiDivider(styleType = PotiDividerStyle.LARGE)
        }
    }
}
