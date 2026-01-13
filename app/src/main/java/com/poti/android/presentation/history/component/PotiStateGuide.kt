package com.poti.android.presentation.history.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors
import com.poti.android.core.designsystem.theme.PotiTheme.typography

@Composable
fun PotiStateGuide(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(colors.poti200),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = typography.body14sb,
            color = colors.poti600,
            modifier = Modifier
                .padding(vertical = 12.dp)
        )
    }
}

@Preview
@Composable
private fun PotiStateGuidePreview() {
    PotiTheme {
        PotiStateGuide(text = "상태 메세지를 입력하세요",
            modifier = Modifier.width(343.dp))
    }
}
