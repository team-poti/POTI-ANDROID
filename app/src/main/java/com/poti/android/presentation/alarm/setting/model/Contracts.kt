package com.poti.android.presentation.alarm.setting.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState

data class AlarmSettingUiState(
    val isTradeEnabled: Boolean = true,
    val isEventEnabled: Boolean = true,
    val showModal: Boolean = false,
) : UiState

sealed interface AlarmSettingUiIntent : UiIntent {
    data object OnBackClick : AlarmSettingUiIntent

    data class OnTradeToggle(val enabled: Boolean) : AlarmSettingUiIntent

    data class OnEventToggle(val enabled: Boolean) : AlarmSettingUiIntent

    data object OnAllowSystemAlarm : AlarmSettingUiIntent

    data object OnModalClose : AlarmSettingUiIntent
}

sealed interface AlarmSettingUiEffect : UiEffect {
    data object NavigateBack : AlarmSettingUiEffect
}
