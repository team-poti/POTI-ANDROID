package com.poti.android.core.designsystem.component.navigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.component.button.PotiIconButton
import com.poti.android.core.designsystem.theme.PotiTheme

/**
 * @param containerColor 헤더 배경색입니다. 화면 배경이 흰색이 아닌 경우 맞춰서 전달합니다.
 */
@Composable
fun PotiHeaderPrimary(
    @DrawableRes firstIconRes: Int,
    onFirstIconClick: () -> Unit,
    @DrawableRes secondIconRes: Int,
    onSecondIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    containerColor: Color = PotiTheme.colors.white,
) {
    Row(
        modifier = modifier
            .background(containerColor)
            .padding(start = 20.dp, end = 4.dp)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (title != null) {
            Text(
                text = title,
                style = PotiTheme.typography.title18sb,
                color = PotiTheme.colors.black,
            )
        } else {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_poti_logo),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        PotiIconButton(
            iconRes = firstIconRes,
            onClick = onFirstIconClick,
            tint = PotiTheme.colors.black,
        )

        PotiIconButton(
            iconRes = secondIconRes,
            onClick = onSecondIconClick,
            tint = PotiTheme.colors.black,
        )
    }
}

/**
 * 두 개의 텍스트 탭으로 구성된 헤더입니다.
 *
 * 뒤로가기 없이 최상위 탭 화면 상단에 사용하며, 선택된 탭만 강조합니다.
 * 예: 분철 내역 탭의 모집내역 / 참여내역 전환
 *
 * @param firstText 첫 번째 탭 텍스트입니다.
 * @param secondText 두 번째 탭 텍스트입니다.
 * @param firstSelected 첫 번째 탭의 선택 여부입니다. false면 두 번째 탭이 선택된 상태입니다.
 * @param onFirstClick 첫 번째 탭 클릭 시 호출됩니다.
 * @param onSecondClick 두 번째 탭 클릭 시 호출됩니다.
 * @param modifier
 * @param containerColor 헤더 배경색입니다. 화면 배경이 흰색이 아닌 경우 맞춰서 전달합니다.
 *
 * @sample PotiHeaderPrimaryTogglePreview
 */
@Composable
fun PotiHeaderPrimaryToggle(
    firstText: String,
    secondText: String,
    firstSelected: Boolean,
    onFirstClick: () -> Unit,
    onSecondClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = PotiTheme.colors.white,
) {
    Row(
        modifier = modifier
            .heightIn(min = 56.dp)
            .background(containerColor)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        PotiHeaderPrimaryToggleItem(
            text = firstText,
            selected = firstSelected,
            onClick = onFirstClick,
        )

        PotiHeaderPrimaryToggleItem(
            text = secondText,
            selected = !firstSelected,
            onClick = onSecondClick,
        )
    }
}

@Composable
private fun PotiHeaderPrimaryToggleItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier.noRippleClickable(onClick = onClick),
        style = PotiTheme.typography.title18sb,
        color = if (selected) PotiTheme.colors.black else PotiTheme.colors.gray500,
    )
}

@Preview
@Composable
private fun PotiHeaderPrimaryPreview() {
    PotiTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PotiHeaderPrimary(
                firstIconRes = R.drawable.ic_search,
                onFirstIconClick = {},
                secondIconRes = R.drawable.ic_alarm,
                onSecondIconClick = {},
            )
            PotiHeaderPrimary(
                title = "마이",
                firstIconRes = R.drawable.ic_setting,
                onFirstIconClick = {},
                secondIconRes = R.drawable.ic_alarm,
                onSecondIconClick = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PotiHeaderPrimaryTogglePreview() {
    var firstSelected by remember { mutableStateOf(true) }

    PotiTheme {
        PotiHeaderPrimaryToggle(
            firstText = stringResource(R.string.header_primary_history_recruit),
            secondText = stringResource(R.string.header_primary_history_participate),
            firstSelected = firstSelected,
            onFirstClick = { firstSelected = true },
            onSecondClick = { firstSelected = false },
        )
    }
}
