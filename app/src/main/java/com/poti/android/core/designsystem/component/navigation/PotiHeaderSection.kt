package com.poti.android.core.designsystem.component.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.theme.PotiTheme

enum class PotiHeaderTabType(
    @StringRes val labelResId: Int,
) {
    ONGOING(R.string.header_section_ongoing),
    ENDED(R.string.header_section_ended),
}

@Composable
fun PotiHeaderSection(
    selectedTab: PotiHeaderTabType,
    ongoingCount: Int,
    endedCount: Int,
    onTabSelected: (PotiHeaderTabType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PotiHeaderTabType.entries.forEach { tabType ->
            val count = when (tabType) {
                PotiHeaderTabType.ONGOING -> ongoingCount
                PotiHeaderTabType.ENDED -> endedCount
            }

            PotiHeaderSectionChip(
                tabType = tabType,
                count = count,
                selected = selectedTab == tabType,
                onClick = { onTabSelected(tabType) },
            )
        }
    }
}

@Composable
private fun PotiHeaderSectionChip(
    tabType: PotiHeaderTabType,
    count: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (selected) PotiTheme.colors.gray900 else PotiTheme.colors.gray100
    val contentColor = if (selected) PotiTheme.colors.white else PotiTheme.colors.gray900

    Row(
        modifier = modifier
            .heightIn(min = 36.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .selectable(
                selected = selected,
                interactionSource = null,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(tabType.labelResId, count),
            style = PotiTheme.typography.body14sb,
            color = contentColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PotiHeaderSectionPreview() {
    var selectedTab by remember { mutableStateOf(PotiHeaderTabType.ONGOING) }

    PotiTheme {
        PotiHeaderSection(
            selectedTab = selectedTab,
            ongoingCount = 3,
            endedCount = 3,
            onTabSelected = { selectedTab = it },
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth(),
        )
    }
}
