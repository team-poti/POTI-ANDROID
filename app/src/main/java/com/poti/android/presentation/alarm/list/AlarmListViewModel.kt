package com.poti.android.presentation.alarm.list

import androidx.lifecycle.viewModelScope
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.notification.Notification
import com.poti.android.domain.usecase.notification.GetNotificationsUseCase
import com.poti.android.presentation.alarm.list.model.AlarmListUiEffect
import com.poti.android.presentation.alarm.list.model.AlarmListUiIntent
import com.poti.android.presentation.alarm.list.model.AlarmListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ALARM_PAGE_SIZE = 20

@HiltViewModel
class AlarmListViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
) : BaseViewModel<AlarmListUiState, AlarmListUiIntent, AlarmListUiEffect>(
        initialState = AlarmListUiState(),
    ) {
    init {
        loadAlarms()
    }

    override fun processIntent(intent: AlarmListUiIntent) {
        when (intent) {
            AlarmListUiIntent.OnBackClick -> sendEffect(AlarmListUiEffect.NavigateBack)
            AlarmListUiIntent.OnSettingClick -> sendEffect(AlarmListUiEffect.NavigateToSetting)
            is AlarmListUiIntent.OnAlarmClick -> handleAlarmClick(intent.alarm)
        }
    }

    private fun loadAlarms() {
        updateState { copy(alarmsLoadState = ApiState.Loading) }

        viewModelScope.launch {
            getNotificationsUseCase(
                page = 0,
                size = ALARM_PAGE_SIZE,
            ).onSuccess { notificationList ->
                updateState {
                    copy(
                        alarmsLoadState = ApiState.Success(
                            notificationList.notifications.toImmutableList(),
                        ),
                    )
                }
            }.onFailure { error ->
                updateState {
                    copy(
                        alarmsLoadState = ApiState.Failure(
                            error.message ?: "Failed to load notifications",
                        ),
                    )
                }
            }
        }
    }

    private fun handleAlarmClick(alarm: Notification) {}
}
