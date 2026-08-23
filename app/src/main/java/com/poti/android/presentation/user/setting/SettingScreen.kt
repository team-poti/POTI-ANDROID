package com.poti.android.presentation.user.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.designsystem.component.button.PotiMenuButton
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.user.setting.model.SettingUiEffect
import com.poti.android.presentation.user.setting.model.SettingUiIntent

@Composable
fun SettingRoute(
    onPopBackStack: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onNavigateToProfileManagement: () -> Unit,
    onNavigateToAddressManagement: () -> Unit,
    onNavigateToAlarmSetting: () -> Unit,
    onNavigateToPersonalInfoPrivacy: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            SettingUiEffect.NavigateBack -> onPopBackStack()
            SettingUiEffect.NavigateToAccount -> onNavigateToAccount()
            SettingUiEffect.NavigateToProfileManagement -> onNavigateToProfileManagement()
            SettingUiEffect.NavigateToAddressManagement -> onNavigateToAddressManagement()
            SettingUiEffect.NavigateToAlarmSetting -> onNavigateToAlarmSetting()
            SettingUiEffect.NavigateToPersonalInfoPrivacy -> onNavigateToPersonalInfoPrivacy()
        }
    }

    SettingScreen(
        appVersion = uiState.appVersion,
        onBackClick = { viewModel.processIntent(SettingUiIntent.OnBackClick) },
        onAccountClick = { viewModel.processIntent(SettingUiIntent.OnAccountClick) },
        onProfileManagementClick = { viewModel.processIntent(SettingUiIntent.OnProfileManagementClick) },
        onAddressManagementClick = { viewModel.processIntent(SettingUiIntent.OnAddressManagementClick) },
        onAlarmClick = { viewModel.processIntent(SettingUiIntent.OnAlarmClick) },
        onPersonalInfoPrivacyClick = { viewModel.processIntent(SettingUiIntent.OnPersonalInfoPrivacyClick) },
        modifier = modifier,
    )
}

@Composable
private fun SettingScreen(
    appVersion: String,
    onBackClick: () -> Unit,
    onAccountClick: () -> Unit,
    onProfileManagementClick: () -> Unit,
    onAddressManagementClick: () -> Unit,
    onAlarmClick: () -> Unit,
    onPersonalInfoPrivacyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        PotiHeaderPage(
            onNavigationClick = onBackClick,
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
                text = stringResource(R.string.setting_menu_account),
                onClick = onAccountClick,
                modifier = Modifier.padding(horizontal = 8.dp),
            )

            PotiMenuButton(
                text = stringResource(R.string.setting_menu_profile_management),
                onClick = onProfileManagementClick,
                modifier = Modifier.padding(horizontal = 8.dp),
            )

            PotiMenuButton(
                text = stringResource(R.string.setting_menu_address_management),
                onClick = onAddressManagementClick,
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
                onClick = onAlarmClick,
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
                onClick = onPersonalInfoPrivacyClick,
                modifier = Modifier.padding(horizontal = 8.dp),
            )

            PotiMenuButton(
                text = stringResource(R.string.version_info),
                onClick = {},
                modifier = Modifier.padding(horizontal = 8.dp),
                trailingText = appVersion,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingScreenPreview() {
    SettingScreen(
        appVersion = "1.0.0",
        onBackClick = {},
        onAccountClick = {},
        onProfileManagementClick = {},
        onAddressManagementClick = {},
        onAlarmClick = {},
        onPersonalInfoPrivacyClick = {},
    )
}
