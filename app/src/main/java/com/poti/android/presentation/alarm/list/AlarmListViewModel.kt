package com.poti.android.presentation.alarm.list

import com.poti.android.core.base.BaseViewModel
import com.poti.android.domain.model.notification.Notification
import com.poti.android.presentation.alarm.list.model.AlarmListUiEffect
import com.poti.android.presentation.alarm.list.model.AlarmListUiIntent
import com.poti.android.presentation.alarm.list.model.AlarmListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlarmListViewModel @Inject constructor() : BaseViewModel<AlarmListUiState, AlarmListUiIntent, AlarmListUiEffect>(
    initialState = AlarmListUiState(),
) {
    override fun processIntent(intent: AlarmListUiIntent) {
        when (intent) {
            AlarmListUiIntent.OnBackClick -> sendEffect(AlarmListUiEffect.NavigateBack)
            AlarmListUiIntent.OnSettingClick -> sendEffect(AlarmListUiEffect.NavigateToSetting)
            is AlarmListUiIntent.OnAlarmClick -> handleAlarmClick(intent.alarm)
        }
    }

    private fun handleAlarmClick(alarm: Notification) {}
}
