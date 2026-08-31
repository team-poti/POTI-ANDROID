package com.poti.android.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poti.android.core.permission.PermissionManager
import com.poti.android.core.permission.PermissionRequestRoute
import com.poti.android.domain.model.auth.AuthState
import com.poti.android.domain.usecase.auth.EnterGuestModeUseCase
import com.poti.android.domain.usecase.auth.IsGuestUseCase
import com.poti.android.domain.usecase.auth.ObserveAuthStateUseCase
import com.poti.android.presentation.auth.navigation.AuthRoute
import com.poti.android.presentation.party.PartyGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val isGuestUseCase: IsGuestUseCase,
    private val enterGuestModeUseCase: EnterGuestModeUseCase,
    private val permissionManager: PermissionManager,
) : ViewModel() {
    private val _showPermissionModal = MutableStateFlow(false)
    val showPermissionModal: StateFlow<Boolean> = _showPermissionModal.asStateFlow()

    private val _permissionRequestRoute = Channel<PermissionRequestRoute>()
    val permissionRequestRoute: Flow<PermissionRequestRoute> = _permissionRequestRoute.receiveAsFlow()

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

    fun checkNotificationPermission() {
        viewModelScope.launch {
            _showPermissionModal.value = permissionManager.shouldShowPermissionModal()
        }
    }

    fun allowNotificationPermission(shouldShowRationale: Boolean) {
        _showPermissionModal.value = false

        viewModelScope.launch {
            _permissionRequestRoute.send(
                permissionManager.requestNotificationPermission(shouldShowRationale),
            )
        }
    }

    fun dismissPermissionModal() {
        _showPermissionModal.value = false
    }

    fun applySystemPermissionDialogResult(isGranted: Boolean) {
        viewModelScope.launch {
            permissionManager.applySystemPermissionDialogResult(isGranted)
        }
    }

    fun syncSystemNotificationPermission() {
        viewModelScope.launch {
            permissionManager.syncSystemPermissionToServer()
        }
    }
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
