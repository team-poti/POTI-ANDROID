package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.typography
import com.poti.android.core.designsystem.theme.SementicRed
import com.poti.android.core.designsystem.theme.White

enum class PotiTagSize {
    SMALL,
    LARGE;

    @Composable
    fun toTextStyle() = when (this) {
        SMALL -> typography.caption10m
        LARGE -> typography.caption12m
    }
}

@Composable
fun PotiTag(
    text: String,
    size: PotiTagSize,
    modifier: Modifier = Modifier
) {
    val textStyle = size.toTextStyle()

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(99.dp))
            .background(White)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = textStyle,
            color = SementicRed
        )
    }
}

@Preview
@Composable
private fun PotiTagPreview() {
    PotiTheme {
        Column {
            PotiTag(text = "인기", size = PotiTagSize.SMALL)
            PotiTag(text = "인기", size = PotiTagSize.LARGE)
        }
    }
}
