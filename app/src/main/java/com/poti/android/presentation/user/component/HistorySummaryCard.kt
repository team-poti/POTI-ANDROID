package com.poti.android.presentation.user.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.user.HistorySummary
import kotlinx.serialization.Serializable

@Serializable
enum class HistorySummaryType {
    IN_PROGRESS,
    COMPLETED,
}

/**
 * @param onItemClick null이면 항목을 클릭할 수 없습니다.
 */
@Composable
fun HistorySummaryCard(
    title: String,
    summary: HistorySummary,
    modifier: Modifier = Modifier,
    onItemClick: ((HistorySummaryType) -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(PotiTheme.colors.white)
            .padding(horizontal = 12.dp)
            .padding(top = 16.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            color = PotiTheme.colors.black,
            style = PotiTheme.typography.body14sb,
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            HistoryItem(
                title = stringResource(R.string.user_history_ongoing),
                count = summary.inProgress,
                onClick = onItemClick?.let { { it(HistorySummaryType.IN_PROGRESS) } },
                colored = true,
            )

            PotiDivider(styleType = PotiDividerStyle.SMALL)

            HistoryItem(
                title = stringResource(R.string.user_history_ended),
                count = summary.completed,
                onClick = onItemClick?.let { { it(HistorySummaryType.COMPLETED) } },
                colored = false,
            )
        }
    }
}

/**
 * @param colored 카운트를 강조 색으로 표시할지 여부입니다. 진행중은 true, 종료는 false를 사용합니다.
 * @param onClick null이면 클릭 및 pressed 효과가 적용되지 않습니다.
 */
@Composable
private fun HistoryItem(
    title: String,
    count: Int,
    onClick: (() -> Unit)?,
    colored: Boolean,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundColor = if (isPressed) PotiTheme.colors.gray100 else PotiTheme.colors.white
    val countColor = if (colored) PotiTheme.colors.poti600 else PotiTheme.colors.black

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .then(
                if (onClick != null) {
                    Modifier.noRippleClickable(
                        interactionSource = interactionSource,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                },
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            color = PotiTheme.colors.gray900,
            style = PotiTheme.typography.body14sb,
        )
        Text(
            text = count.toString(),
            color = countColor,
            style = PotiTheme.typography.display18b,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HistorySummaryCardPreview() {
    val summary = HistorySummary(
        inProgress = 2,
        completed = 5,
    )

    PotiTheme {
        Row(
            modifier = Modifier
                .background(PotiTheme.colors.gray100)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            HistorySummaryCard(
                title = "참여 내역",
                summary = summary,
                onItemClick = {},
                modifier = Modifier.width(158.dp),
            )

            HistorySummaryCard(
                title = "모집 내역",
                summary = summary,
                modifier = Modifier.width(158.dp),
            )
        }
    }
}
