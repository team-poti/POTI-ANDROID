package com.poti.android.presentation.user.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

@Composable
fun AccountSettingRoute(modifier: Modifier = Modifier) {
}

@Composable
fun AccountSettingScreen(
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        PotiHeaderPage(
            onNavigationClick = {},
            title = stringResource(R.string.setting_my_account),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            PotiMenuButton(
                text = stringResource(R.string.account_name),
                onClick = {},
                modifier = Modifier.padding(horizontal = 8.dp),
                trailingText = "",
            )

            PotiMenuButton(
                text = stringResource(R.string.account_email),
                onClick = {},
                modifier = Modifier.padding(horizontal = 8.dp),
                trailingText = "",
            )

            PotiMenuButton(
                text = stringResource(R.string.social_account),
                onClick = {},
                modifier = Modifier.padding(horizontal = 8.dp),
                trailingText = "",
            )

            PotiDivider(
                PotiDividerStyle.LARGE,
                modifier = Modifier.padding(vertical = 8.dp),
            )

            PotiMenuButton(
                text = stringResource(R.string.account_logout),
                onClick = {},
                modifier = Modifier.padding(horizontal = 8.dp),
            )

            PotiMenuButton(
                text = stringResource(R.string.account_withdrawal),
                onClick = {},
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AccountSettingScreenPreview() {
    AccountSettingScreen()
}
