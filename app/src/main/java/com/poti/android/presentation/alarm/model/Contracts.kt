package com.poti.android.presentation.alarm.model

import androidx.compose.runtime.Immutable
import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.notification.Notification
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class AlarmUiState(
    val alarmsLoadState: ApiState<ImmutableList<Notification>> = ApiState.Init,
) : UiState

sealed interface AlarmUiIntent : UiIntent {
    data object OnBackClick : AlarmUiIntent

    data object OnSettingClick : AlarmUiIntent

    data class OnAlarmClick(val alarm: Notification) : AlarmUiIntent
}

sealed interface AlarmUiEffect : UiEffect {
    data object NavigateBack : AlarmUiEffect

    data object NavigateToSetting : AlarmUiEffect
}
