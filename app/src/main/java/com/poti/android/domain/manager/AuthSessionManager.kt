package com.poti.android.domain.manager

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthSessionManager @Inject constructor() {
    private val _logoutEvent = MutableSharedFlow<Unit>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val logoutEvent = _logoutEvent.asSharedFlow()

    private val _isGuest = MutableStateFlow(false)
    val isGuest: StateFlow<Boolean> = _isGuest.asStateFlow()

    private var pendingReturnDeepLink: String? = null

    fun triggerLogout() {
        _logoutEvent.tryEmit(Unit)
    }

    fun enterGuest() {
        _isGuest.value = true
    }

    fun exitGuest() {
        _isGuest.value = false
    }

    fun setPendingReturnDeepLink(deepLink: String) {
        pendingReturnDeepLink = deepLink
    }

    fun consumePendingReturnDeepLink(): String? =
        pendingReturnDeepLink.also { pendingReturnDeepLink = null }
}
