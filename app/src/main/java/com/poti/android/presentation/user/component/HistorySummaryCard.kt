package com.poti.android.presentation.user.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.theme.PotiTheme

enum class HistorySummaryType {
    PARTICIPATED_ALL,
    PARTICIPATED_IN_PROGRESS,
    PARTICIPATED_FINISHED,
    RECRUITED_ALL,
    RECRUITED_IN_PROGRESS,
    RECRUITED_FINISHED,
}

@Composable
fun HistorySummaryCard(
    title: String,
    totalCount: Int,
    inProgressCount: Int,
    finishedCount: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .widthIn(min = 328.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = title,
            color = PotiTheme.colors.black,
            style = PotiTheme.typography.body16sb,
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(PotiTheme.colors.gray100)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HistoryItem(
                title = "전체",
                count = totalCount,
                onClick = { },
                modifier = Modifier.weight(1f),
            )

            DividerSm(
                modifier = Modifier
                    .height(56.dp)
                    .padding(horizontal = 8.dp),
            )

            HistoryItem(
                title = "진행중",
                count = inProgressCount,
                onClick = { },
                modifier = Modifier.weight(1f),
            )

            DividerSm(
                modifier = Modifier
                    .height(56.dp)
                    .padding(horizontal = 8.dp),
            )

            HistoryItem(
                title = "종료",
                count = finishedCount,
                onClick = { },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun HistoryItem(
    title: String,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundColor = if (isPressed) PotiTheme.colors.gray300 else PotiTheme.colors.gray100

    Column(
        modifier = modifier
            .heightIn(88.dp)
            .widthIn(92.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .noRippleClickable(
                interactionSource = interactionSource,
                onClick = onClick,
            )
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = count.toString(),
            color = PotiTheme.colors.poti600,
            style = PotiTheme.typography.title18sb,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            color = PotiTheme.colors.gray800,
            style = PotiTheme.typography.caption12m,
        )
    }
}

// TODO: [예림] 공통 컴포넌트로 변경
@Composable
private fun DividerSm(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(1.dp)
            .background(PotiTheme.colors.gray300),
    )
}

@Preview(showBackground = true)
@Composable
private fun HistorySummaryCardPreview() {
    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            HistorySummaryCard(
                title = "참여 내역",
                totalCount = 3,
                inProgressCount = 2,
                finishedCount = 1,
                modifier = Modifier.width(328.dp),
            )

            HistorySummaryCard(
                title = "참여 내역",
                totalCount = 3,
                inProgressCount = 2,
                finishedCount = 1,
                modifier = Modifier.width(440.dp),
            )
        }
    }
}
