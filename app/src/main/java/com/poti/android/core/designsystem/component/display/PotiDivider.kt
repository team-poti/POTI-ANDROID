package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.PotiTheme

enum class PotiDividerSize(val height: Dp, val color: @Composable () -> androidx.compose.ui.graphics.Color) {
    SMALL(height = 1.dp, color = { PotiTheme.colors.gray300 }),
    LARGE(height = 8.dp, color = { PotiTheme.colors.gray100 })
}

@Composable
fun PotiDivider(
    size: PotiDividerSize,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(size.height)
            .background(size.color())
    )
}

@Preview(showBackground = true)
@Composable
private fun PotiDividerPreview() {
    PotiTheme {
        Column {
            PotiDivider(size = PotiDividerSize.SMALL)

            Spacer(Modifier.height(20.dp))

            PotiDivider(size = PotiDividerSize.LARGE)
        }
    }
}
