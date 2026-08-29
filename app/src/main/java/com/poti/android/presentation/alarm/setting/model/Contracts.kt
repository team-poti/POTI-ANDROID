package com.poti.android.presentation.alarm.setting.model

import androidx.annotation.StringRes
import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState

data class AlarmSettingUiState(
    val isTradeEnabled: Boolean = true,
    val isEventEnabled: Boolean = true,
    val showModal: Boolean = false,
    val updateState: ApiState<Unit> = ApiState.Init,
) : UiState

sealed interface AlarmSettingUiIntent : UiIntent {
    data object OnBackClick : AlarmSettingUiIntent

    data class OnTradeToggle(
        val enabled: Boolean,
        val isSystemNotificationEnabled: Boolean,
    ) : AlarmSettingUiIntent

    data class OnEventToggle(
        val enabled: Boolean,
        val isSystemNotificationEnabled: Boolean,
    ) : AlarmSettingUiIntent

    data object OnAllowSystemAlarm : AlarmSettingUiIntent

    data class OnResume(
        val isSystemNotificationEnabled: Boolean,
    ) : AlarmSettingUiIntent

    data object OnModalClose : AlarmSettingUiIntent
}

sealed interface AlarmSettingUiEffect : UiEffect {
    data object NavigateBack : AlarmSettingUiEffect

    data object OpenSystemNotificationSetting : AlarmSettingUiEffect

    data class ShowToast(
        @param:StringRes val messageRes: Int,
    ) : AlarmSettingUiEffect
}
