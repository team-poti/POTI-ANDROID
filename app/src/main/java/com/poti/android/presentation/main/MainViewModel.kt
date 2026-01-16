package com.poti.android.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poti.android.data.local.datasource.PreferenceDataSource
import com.poti.android.presentation.auth.navigation.AuthRoute
import com.poti.android.presentation.onboarding.navigation.OnboardingRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    preferenceDataSource: PreferenceDataSource,
) : ViewModel() {
    val startDestination = preferenceDataSource.authState
        .map { authState ->
            when {
                authState.accessToken.isNullOrBlank() -> AuthRoute.Login

                !authState.isOnboardingFinished -> OnboardingRoute.Guide

                else -> MainTab.HOME.route
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )
}
