package com.poti.android.presentation.alarm

import com.poti.android.core.base.BaseViewModel
import com.poti.android.domain.model.notification.Notification
import com.poti.android.presentation.alarm.model.AlarmUiEffect
import com.poti.android.presentation.alarm.model.AlarmUiIntent
import com.poti.android.presentation.alarm.model.AlarmUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor() : BaseViewModel<AlarmUiState, AlarmUiIntent, AlarmUiEffect>(
    initialState = AlarmUiState(),
) {
    override fun processIntent(intent: AlarmUiIntent) {
        when (intent) {
            AlarmUiIntent.OnBackClick -> sendEffect(AlarmUiEffect.NavigateBack)
            AlarmUiIntent.OnSettingClick -> sendEffect(AlarmUiEffect.NavigateToSetting)
            is AlarmUiIntent.OnAlarmClick -> handleAlarmClick(intent.alarm)
        }
    }

    private fun handleAlarmClick(alarm: Notification) {}
}
