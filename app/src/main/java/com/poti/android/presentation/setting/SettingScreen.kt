package com.poti.android.presentation.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun SettingRoute(modifier: Modifier = Modifier) {
}

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        PotiHeaderPage(
            onNavigationClick = {},
            title = "헤더 타이틀",
        )

        Text(
            text = "내 정보",
            style = PotiTheme.typography.body14m,
            color = PotiTheme.colors.gray800,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 8.dp),
        )

        // TODO: PotiMenuButton 머지되면 추가

        PotiDivider(
            PotiDividerStyle.LARGE,
            modifier = Modifier.padding(vertical = 8.dp),
        )

        Text(
            text = "앱 설정",
            style = PotiTheme.typography.body14m,
            color = PotiTheme.colors.gray800,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 8.dp),
        )

        // TODO: PotiMenuButton 머지되면 추가

        PotiDivider(
            PotiDividerStyle.LARGE,
            modifier = Modifier.padding(vertical = 8.dp),
        )

        Text(
            text = "서비스 정보",
            style = PotiTheme.typography.body14m,
            color = PotiTheme.colors.gray800,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 8.dp),
        )

        // TODO: PotiMenuButton 머지되면 추가
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingScreenPreview() {
    SettingScreen()
}
