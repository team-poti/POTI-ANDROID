package com.poti.android.presentation.alarm.setting

import com.poti.android.core.base.BaseViewModel
import com.poti.android.presentation.alarm.setting.model.AlarmSettingUiEffect
import com.poti.android.presentation.alarm.setting.model.AlarmSettingUiIntent
import com.poti.android.presentation.alarm.setting.model.AlarmSettingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlarmSettingViewModel @Inject constructor() : BaseViewModel<AlarmSettingUiState, AlarmSettingUiIntent, AlarmSettingUiEffect>(
    initialState = AlarmSettingUiState(),
) {
    override fun processIntent(intent: AlarmSettingUiIntent) {
        when (intent) {
            AlarmSettingUiIntent.OnBackClick -> sendEffect(AlarmSettingUiEffect.NavigateBack)
            is AlarmSettingUiIntent.OnTradeToggle -> updateTradeAlarm(intent.enabled)
            is AlarmSettingUiIntent.OnEventToggle -> updateEventAlarm(intent.enabled)
            AlarmSettingUiIntent.OnAllowSystemAlarm -> requestSystemAlarmPermission()
            AlarmSettingUiIntent.OnModalClose -> updateState { copy(showModal = false) }
        }
    }

    private fun updateTradeAlarm(enabled: Boolean) {
        updateState { copy(isTradeEnabled = enabled) }
        showPermissionModal()
    }

    private fun updateEventAlarm(enabled: Boolean) {
        updateState { copy(isEventEnabled = enabled) }
        showPermissionModal()
    }

    private fun showPermissionModal() {
        updateState { copy(showModal = true) }
    }

    private fun requestSystemAlarmPermission() {}
}
