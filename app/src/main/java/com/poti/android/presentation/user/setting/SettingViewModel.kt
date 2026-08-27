package com.poti.android.presentation.user.setting

import com.poti.android.BuildConfig
import com.poti.android.core.base.BaseViewModel
import com.poti.android.presentation.user.setting.model.SettingUiEffect
import com.poti.android.presentation.user.setting.model.SettingUiIntent
import com.poti.android.presentation.user.setting.model.SettingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor() : BaseViewModel<SettingUiState, SettingUiIntent, SettingUiEffect>(
    initialState = SettingUiState(appVersion = BuildConfig.VERSION_NAME),
) {
    override fun processIntent(intent: SettingUiIntent) {
        when (intent) {
            SettingUiIntent.OnBackClick -> sendEffect(SettingUiEffect.NavigateBack)
            SettingUiIntent.OnAccountClick -> sendEffect(SettingUiEffect.NavigateToAccount)
            SettingUiIntent.OnProfileManagementClick -> sendEffect(SettingUiEffect.NavigateToProfileManagement)
            SettingUiIntent.OnAddressManagementClick -> sendEffect(SettingUiEffect.NavigateToAddressManagement)
            SettingUiIntent.OnAlarmClick -> sendEffect(SettingUiEffect.NavigateToAlarmSetting)
            SettingUiIntent.OnPersonalInfoPrivacyClick -> sendEffect(SettingUiEffect.NavigateToPersonalInfoPrivacy)
        }
    }
}
