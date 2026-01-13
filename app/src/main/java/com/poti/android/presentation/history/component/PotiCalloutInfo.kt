package com.poti.android.presentation.history.component

import android.content.ClipData
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors
import kotlinx.coroutines.launch

@Composable
fun PotiCalloutInfo(
    text: String,
    modifier: Modifier = Modifier,
    copyable: Boolean = true,
) {
    val clipboardManager = LocalClipboard.current
    val localScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(colors.gray100),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                style = PotiTheme.typography.body14m,
                color = colors.black,
                modifier = Modifier.weight(1f),
            )

            if (copyable) {
                Text(
                    text = "복사",
                    style = PotiTheme.typography.body14m.copy(textDecoration = TextDecoration.Underline),
                    color = colors.gray700,
                    modifier = Modifier
                        .noRippleClickable {
                            val clipData = ClipData.newPlainText("", text)
                            val clipEntry = ClipEntry(clipData)
                            localScope.launch {
                                clipboardManager.setClipEntry(clipEntry)
                            }
                        }
                        .padding(start = 12.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PotiCalloutInfoPreview() {
    PotiTheme {
        PotiCalloutInfo(
            text = "정보",
            copyable = true,
        )
    }
}

@Preview
@Composable
private fun PotiCalloutInfoNoCopyPreview() {
    PotiTheme {
        PotiCalloutInfo(
            text = "정보 (복사 불가능)",
            copyable = false,
        )
    }
}
