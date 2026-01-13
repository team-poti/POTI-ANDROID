package com.poti.android.core.designsystem.component.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
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
    modifier: Modifier = Modifier,
    onTabSelected: (PotiHeaderTabType) -> Unit,
) {
    val activeColor = PotiTheme.colors.black
    val inactiveColor = PotiTheme.colors.gray700

    TabRow(
        selectedTabIndex = selectedTab.ordinal,
        containerColor = PotiTheme.colors.white,
        modifier = modifier,
        divider = {
            HorizontalDivider(
                color = Color(0xFFDCDCDC), // TODO: [지현] 컬러 시스템 변경
                thickness = 2.dp,
            )
        },
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                height = 2.dp,
                color = PotiTheme.colors.poti600,
            )
        },
    ) {
        PotiHeaderTabType.entries.forEach { tabType ->
            val isSelected = selectedTab == tabType
            val count = when (tabType) {
                PotiHeaderTabType.ONGOING -> ongoingCount
                PotiHeaderTabType.ENDED -> endedCount
            }

            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .noRippleClickable { onTabSelected(tabType) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = count.toString(),
                    style = PotiTheme.typography.display18b,
                    color = if (isSelected) activeColor else inactiveColor,
                )
                Text(
                    text = stringResource(tabType.labelResId),
                    style = PotiTheme.typography.body14m,
                    color = if (isSelected) activeColor else inactiveColor,
                )
            }
        }
    }
}

@Composable
private fun PotiHeaderSectionTab(modifier: Modifier = Modifier) {
    Column { }
}

@Preview
@Composable
private fun PotiHeaderSectionPreview() {
    var selectedTab by remember { mutableStateOf(PotiHeaderTabType.ONGOING) }

    PotiTheme {
        Column {
            PotiHeaderSection(
                selectedTab = selectedTab,
                ongoingCount = 2,
                endedCount = 5,
                onTabSelected = { selectedTab = it },
            )
        }
    }
}
