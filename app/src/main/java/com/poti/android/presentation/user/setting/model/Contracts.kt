package com.poti.android.presentation.user.setting.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState

data class SettingUiState(
    val appVersion: String = "",
) : UiState

sealed interface SettingUiIntent : UiIntent {
    data object OnBackClick : SettingUiIntent

    data object OnAccountClick : SettingUiIntent

    data object OnProfileManagementClick : SettingUiIntent

    data object OnAddressManagementClick : SettingUiIntent

    data object OnAlarmClick : SettingUiIntent

    data object OnPersonalInfoPrivacyClick : SettingUiIntent
}

sealed interface SettingUiEffect : UiEffect {
    data object NavigateBack : SettingUiEffect

    data object NavigateToAccount : SettingUiEffect

    data object NavigateToProfileManagement : SettingUiEffect

    data object NavigateToAddressManagement : SettingUiEffect

    data object NavigateToAlarmSetting : SettingUiEffect

    data object NavigateToPersonalInfoPrivacy : SettingUiEffect
}
