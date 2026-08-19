package com.poti.android.presentation.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.button.PotiMenuButton
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
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        PotiHeaderPage(
            onNavigationClick = {},
            title = stringResource(R.string.setting_title),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            Text(
                text = stringResource(R.string.setting_my_info),
                style = PotiTheme.typography.body14m,
                color = PotiTheme.colors.gray800,
                modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 8.dp),
            )

            PotiMenuButton(
                text = stringResource(R.string.setting_my_account),
                onClick = {},
                modifier = Modifier.padding(horizontal = 8.dp),
            )

            PotiMenuButton(
                text = stringResource(R.string.setting_my_profile),
                onClick = {},
                modifier = Modifier.padding(horizontal = 8.dp),
            )

            PotiMenuButton(
                text = stringResource(R.string.setting_my_address),
                onClick = {},
                modifier = Modifier.padding(horizontal = 8.dp),
            )

            PotiDivider(
                PotiDividerStyle.LARGE,
                modifier = Modifier.padding(vertical = 8.dp),
            )

            Text(
                text = stringResource(R.string.setting_app),
                style = PotiTheme.typography.body14m,
                color = PotiTheme.colors.gray800,
                modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 8.dp),
            )

            PotiMenuButton(
                text = stringResource(R.string.setting_alarm),
                onClick = {},
                modifier = Modifier.padding(horizontal = 8.dp),
            )

            PotiDivider(
                PotiDividerStyle.LARGE,
                modifier = Modifier.padding(vertical = 8.dp),
            )

            Text(
                text = stringResource(R.string.service_info),
                style = PotiTheme.typography.body14m,
                color = PotiTheme.colors.gray800,
                modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 8.dp),
            )

            PotiMenuButton(
                text = stringResource(R.string.setting_personal_info_privacy),
                onClick = {},
                modifier = Modifier.padding(horizontal = 8.dp),
            )

            PotiMenuButton(
                text = stringResource(R.string.version_info),
                onClick = {},
                modifier = Modifier.padding(horizontal = 8.dp),
                trailingText = "1.0.0",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingScreenPreview() {
    SettingScreen()
}
