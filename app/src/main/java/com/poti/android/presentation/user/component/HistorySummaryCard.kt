package com.poti.android.presentation.user.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import com.poti.android.core.designsystem.theme.PotiTheme
import kotlin.collections.forEachIndexed
import kotlin.collections.lastIndex

enum class HistorySummaryType {
    ALL,
    IN_PROGRESS,
    FINISHED,
}

data class HistoryItemUiModel(
    val type: HistorySummaryType,
    val titleRes: Int,
    val count: Int,
)

@Composable
fun HistorySummaryCard(
    title: String,
    items: List<HistoryItemUiModel>,
    onItemClick: (HistorySummaryType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
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
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEachIndexed { index, item ->

                HistoryItem(
                    title = stringResource(item.titleRes),
                    count = item.count,
                    onClick = { onItemClick(item.type) },
                    modifier = Modifier.weight(1f),
                )

                if (index != items.lastIndex) {
                    VerticalDivider(
                        modifier = Modifier
                            .height(56.dp)
                            .clip(CircleShape),
                        thickness = 1.dp,
                        color = PotiTheme.colors.gray300,
                    )
                }
            }
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

@Preview(showBackground = true)
@Composable
private fun HistorySummaryCardPreview() {
    val items = listOf(
        HistoryItemUiModel(
            type = HistorySummaryType.ALL,
            titleRes = R.string.user_history_all,
            count = 3,
        ),
        HistoryItemUiModel(
            type = HistorySummaryType.IN_PROGRESS,
            titleRes = R.string.user_history_ongoing,
            count = 2,
        ),
        HistoryItemUiModel(
            type = HistorySummaryType.FINISHED,
            titleRes = R.string.user_history_ended,
            count = 1,
        ),
    )

    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            HistorySummaryCard(
                title = "참여 내역",
                items = items,
                onItemClick = {},
                modifier = Modifier.width(328.dp),
            )

            HistorySummaryCard(
                title = "참여 내역",
                items = items,
                onItemClick = {},
                modifier = Modifier,
            )
        }
    }
}
