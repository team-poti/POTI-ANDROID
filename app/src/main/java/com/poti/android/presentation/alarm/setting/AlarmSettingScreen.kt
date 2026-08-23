package com.poti.android.presentation.alarm.setting

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.extension.toast
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.designsystem.component.button.PotiMenuToggle
import com.poti.android.core.designsystem.component.modal.PotiLargeModal
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.alarm.setting.model.AlarmSettingUiEffect
import com.poti.android.presentation.alarm.setting.model.AlarmSettingUiIntent
import com.poti.android.presentation.alarm.setting.model.AlarmSettingUiState
import timber.log.Timber

@Composable
fun AlarmSettingRoute(
    onPopBackStack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AlarmSettingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val isSystemNotificationEnabled = {
        NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.processIntent(
            AlarmSettingUiIntent.OnResume(
                isSystemNotificationEnabled = isSystemNotificationEnabled(),
            ),
        )
    }

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            AlarmSettingUiEffect.NavigateBack -> onPopBackStack()
            AlarmSettingUiEffect.OpenSystemNotificationSetting ->
                context.openSystemNotificationSetting()
            is AlarmSettingUiEffect.ShowToast -> context.toast(context.getString(effect.messageRes))
        }
    }

    if (uiState.showModal) {
        PotiLargeModal(
            onDismissRequest = { viewModel.processIntent(AlarmSettingUiIntent.OnModalClose) },
            title = stringResource(R.string.alarm_setting_permission_modal_title),
            text = stringResource(R.string.alarm_setting_permission_modal_text),
            btnText = stringResource(R.string.alarm_setting_permission_modal_button),
            onBtnClick = { viewModel.processIntent(AlarmSettingUiIntent.OnAllowSystemAlarm) },
            subBtnText = stringResource(R.string.alarm_setting_permission_modal_sub_button),
            onSubBtnClick = { viewModel.processIntent(AlarmSettingUiIntent.OnModalClose) },
            dismissOnClickOutside = false,
            topSlot = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_alarm),
                    contentDescription = null,
                    tint = PotiTheme.colors.poti600,
                    modifier = Modifier.size(48.dp),
                )

                Spacer(Modifier.height(12.dp))
            },
            content = { Spacer(Modifier.height(24.dp)) },
        )
    }

    AlarmSettingScreen(
        uiState = uiState,
        onBackClick = { viewModel.processIntent(AlarmSettingUiIntent.OnBackClick) },
        onTradeToggle = { enabled ->
            viewModel.processIntent(
                AlarmSettingUiIntent.OnTradeToggle(
                    enabled = enabled,
                    isSystemNotificationEnabled = isSystemNotificationEnabled(),
                ),
            )
        },
        onEventToggle = { enabled ->
            viewModel.processIntent(
                AlarmSettingUiIntent.OnEventToggle(
                    enabled = enabled,
                    isSystemNotificationEnabled = isSystemNotificationEnabled(),
                ),
            )
        },
        modifier = modifier,
    )
}

private fun Context.openSystemNotificationSetting() {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)

    runCatching { startActivity(intent) }
        .onFailure { Timber.w(it, "Unable to open system notification setting") }
}

@Composable
private fun AlarmSettingScreen(
    uiState: AlarmSettingUiState,
    onBackClick: () -> Unit,
    onTradeToggle: (Boolean) -> Unit,
    onEventToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        PotiHeaderPage(
            onNavigationClick = onBackClick,
            title = stringResource(R.string.alarm_setting_title),
        )

        PotiMenuToggle(
            title = stringResource(R.string.alarm_setting_trade_title),
            description = stringResource(R.string.alarm_setting_trade_description),
            checked = uiState.isTradeEnabled,
            onCheckedChange = onTradeToggle,
            modifier = Modifier.fillMaxWidth(),
        )

        PotiMenuToggle(
            title = stringResource(R.string.alarm_setting_event_title),
            description = stringResource(R.string.alarm_setting_event_description),
            checked = uiState.isEventEnabled,
            onCheckedChange = onEventToggle,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AlarmSettingScreenPreview() {
    PotiTheme {
        AlarmSettingScreen(
            uiState = AlarmSettingUiState(
                isTradeEnabled = true,
                isEventEnabled = false,
            ),
            onBackClick = {},
            onTradeToggle = {},
            onEventToggle = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
