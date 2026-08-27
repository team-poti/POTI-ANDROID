package com.poti.android.domain.usecase.auth

import com.poti.android.domain.manager.AuthSessionManager
import javax.inject.Inject

class EnterGuestModeUseCase @Inject constructor(
    private val authSessionManager: AuthSessionManager,
) {
    operator fun invoke() = authSessionManager.enterGuest()
}
