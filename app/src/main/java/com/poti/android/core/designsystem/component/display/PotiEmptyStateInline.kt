package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PotiEmptyStateInline(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = PotiTheme.typography.body14m,
        color = PotiTheme.colors.gray700,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 52.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun PotiEmptyStateInlinePreview() {
    PotiTheme {
        Column {
            PotiEmptyStateInline(text = "현재 참여 중인 사용자가 없어요")
        }
    }
}
