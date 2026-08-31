package com.poti.android.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poti.android.domain.model.auth.AuthState
import com.poti.android.domain.usecase.auth.EnterGuestModeUseCase
import com.poti.android.domain.usecase.auth.IsGuestUseCase
import com.poti.android.domain.usecase.auth.ObserveAuthStateUseCase
import com.poti.android.presentation.auth.navigation.AuthRoute
import com.poti.android.presentation.party.PartyGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val isGuestUseCase: IsGuestUseCase,
    private val enterGuestModeUseCase: EnterGuestModeUseCase,
) : ViewModel() {
    fun isGuest(): Boolean = isGuestUseCase()

    private val authState = observeAuthStateUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = AuthState(
                accessToken = null,
                isOnboardingFinished = false,
            ),
        )

    val startDestination = authState
        .map { authState ->
            when {
                !authState.isInitialized -> null
                !authState.accessToken.isNullOrBlank() && authState.isOnboardingFinished -> PartyGraph
                else -> AuthRoute.Login
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    internal fun getDeepLinkEntryMode(): DeepLinkEntryMode = authState.value.toDeepLinkEntryMode()

    internal fun enterGuestMode() = enterGuestModeUseCase()
}

internal enum class DeepLinkEntryMode {
    DEFER,
    MEMBER,
    GUEST,
}

internal fun AuthState.toDeepLinkEntryMode(): DeepLinkEntryMode = when {
    !isInitialized -> DeepLinkEntryMode.DEFER
    !accessToken.isNullOrBlank() && !isOnboardingFinished -> DeepLinkEntryMode.DEFER
    !accessToken.isNullOrBlank() -> DeepLinkEntryMode.MEMBER
    else -> DeepLinkEntryMode.GUEST
}
