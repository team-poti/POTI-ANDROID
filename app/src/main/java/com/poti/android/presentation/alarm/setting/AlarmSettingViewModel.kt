package com.poti.android.presentation.alarm.setting

import androidx.lifecycle.viewModelScope
import com.poti.android.R
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.usecase.notification.GetNotificationSettingUseCase
import com.poti.android.domain.usecase.notification.UpdateNotificationSettingUseCase
import com.poti.android.presentation.alarm.setting.model.AlarmSettingUiEffect
import com.poti.android.presentation.alarm.setting.model.AlarmSettingUiIntent
import com.poti.android.presentation.alarm.setting.model.AlarmSettingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmSettingViewModel @Inject constructor(
    private val getNotificationSettingUseCase: GetNotificationSettingUseCase,
    private val updateNotificationSettingUseCase: UpdateNotificationSettingUseCase,
) : BaseViewModel<AlarmSettingUiState, AlarmSettingUiIntent, AlarmSettingUiEffect>(
        initialState = AlarmSettingUiState(),
    ) {
    init {
        loadAlarmSetting()
    }

    override fun processIntent(intent: AlarmSettingUiIntent) {
        when (intent) {
            AlarmSettingUiIntent.OnBackClick -> sendEffect(AlarmSettingUiEffect.NavigateBack)
            is AlarmSettingUiIntent.OnTradeToggle -> updateTradeAlarm(intent)
            is AlarmSettingUiIntent.OnEventToggle -> updateEventAlarm(intent)
            AlarmSettingUiIntent.OnAllowSystemAlarm -> requestSystemAlarmPermission()
            AlarmSettingUiIntent.OnModalClose -> updateState { copy(showModal = false) }
        }
    }

    private fun loadAlarmSetting() {
        viewModelScope.launch {
            getNotificationSettingUseCase().onSuccess { setting ->
                updateState {
                    copy(
                        isTradeEnabled = setting.isTradeEnabled,
                        isEventEnabled = setting.isEventEnabled,
                    )
                }
            }.onFailure { _ ->
                sendEffect(AlarmSettingUiEffect.ShowToast(R.string.alarm_setting_load_failed))
            }
        }
    }

    private fun updateTradeAlarm(intent: AlarmSettingUiIntent.OnTradeToggle) {
        updateAlarmSetting(
            isTradeEnabled = intent.enabled,
            isEventEnabled = uiState.value.isEventEnabled,
            isTurnedOn = intent.enabled,
            isSystemNotificationEnabled = intent.isSystemNotificationEnabled,
        )
    }

    private fun updateEventAlarm(intent: AlarmSettingUiIntent.OnEventToggle) {
        updateAlarmSetting(
            isTradeEnabled = uiState.value.isTradeEnabled,
            isEventEnabled = intent.enabled,
            isTurnedOn = intent.enabled,
            isSystemNotificationEnabled = intent.isSystemNotificationEnabled,
        )
    }

    private fun updateAlarmSetting(
        isTradeEnabled: Boolean,
        isEventEnabled: Boolean,
        isTurnedOn: Boolean,
        isSystemNotificationEnabled: Boolean,
    ) {
        if (uiState.value.updateState is ApiState.Loading) return

        val previousSetting = uiState.value

        updateState {
            copy(
                isTradeEnabled = isTradeEnabled,
                isEventEnabled = isEventEnabled,
                updateState = ApiState.Loading,
            )
        }

        viewModelScope.launch {
            updateNotificationSettingUseCase(
                isTradeEnabled = isTradeEnabled,
                isEventEnabled = isEventEnabled,
            ).onSuccess { _ ->
                updateState { copy(updateState = ApiState.Success(Unit)) }

                if (isTurnedOn && !isSystemNotificationEnabled) {
                    showPermissionModal()
                }
            }.onFailure { error ->
                updateState {
                    copy(
                        isTradeEnabled = previousSetting.isTradeEnabled,
                        isEventEnabled = previousSetting.isEventEnabled,
                        updateState = ApiState.Failure(error.toString()),
                    )
                }
                sendEffect(AlarmSettingUiEffect.ShowToast(R.string.alarm_setting_update_failed))
            }
        }
    }

    private fun showPermissionModal() {
        updateState { copy(showModal = true) }
    }

    private fun requestSystemAlarmPermission() {}
}
