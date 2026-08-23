package com.poti.android.presentation.alarm.list.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.notification.Notification
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class AlarmListUiState(
    val alarmsLoadState: ApiState<ImmutableList<Notification>> = ApiState.Init,
) : UiState

sealed interface AlarmListUiIntent : UiIntent {
    data object OnBackClick : AlarmListUiIntent

    data object OnSettingClick : AlarmListUiIntent

    data class OnAlarmClick(val alarm: Notification) : AlarmListUiIntent
}

sealed interface AlarmListUiEffect : UiEffect {
    data object NavigateBack : AlarmListUiEffect

    data object NavigateToSetting : AlarmListUiEffect

    data class OpenDeepLink(
        val deepLink: String,
    ) : AlarmListUiEffect

    data class ShowToast(
        @param:StringRes val messageRes: Int,
    ) : AlarmListUiEffect
}
