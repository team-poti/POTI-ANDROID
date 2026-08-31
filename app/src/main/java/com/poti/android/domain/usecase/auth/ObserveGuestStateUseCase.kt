package com.poti.android.domain.usecase.auth

import com.poti.android.domain.manager.AuthSessionManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveGuestStateUseCase @Inject constructor(
    private val authSessionManager: AuthSessionManager,
) {
    operator fun invoke(): Flow<Boolean> = authSessionManager.isGuest
}
