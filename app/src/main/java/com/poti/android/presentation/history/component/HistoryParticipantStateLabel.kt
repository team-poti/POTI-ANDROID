package com.poti.android.presentation.history.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors
import com.poti.android.core.designsystem.theme.PotiTheme.typography

enum class ParticipantStateLabelColor {
    RED,
    BLUE,
    GRAY,
}

val ParticipantStateLabelColor.color: Color
    @Composable get() = when (this) {
        ParticipantStateLabelColor.RED -> colors.sementicRed
        ParticipantStateLabelColor.BLUE -> colors.poti600
        ParticipantStateLabelColor.GRAY -> colors.gray700
    }

enum class ParticipantStateLabelSize {
    LARGE,
    SMALL,
}

val ParticipantStateLabelSize.style: TextStyle
    @Composable get() = when (this) {
        ParticipantStateLabelSize.LARGE -> typography.body16sb
        ParticipantStateLabelSize.SMALL -> typography.body14sb
    }

@Composable
fun HistoryParticipantStateLabel(
    text: String,
    sizeType: ParticipantStateLabelSize,
    colorType: ParticipantStateLabelColor,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier
            .height(24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = sizeType.style,
            color = colorType.color,
            modifier = modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryParticipantStateLabelPreview() {
    PotiTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            HistoryParticipantStateLabel(
                text = "입금 대기",
                sizeType = ParticipantStateLabelSize.LARGE,
                colorType = ParticipantStateLabelColor.RED,
            )
            HistoryParticipantStateLabel(
                text = "입금 완료",
                sizeType = ParticipantStateLabelSize.SMALL,
                colorType = ParticipantStateLabelColor.GRAY,
            )
            HistoryParticipantStateLabel(
                text = "모집 완료",
                sizeType = ParticipantStateLabelSize.SMALL,
                colorType = ParticipantStateLabelColor.BLUE,
            )
        }
    }
}
