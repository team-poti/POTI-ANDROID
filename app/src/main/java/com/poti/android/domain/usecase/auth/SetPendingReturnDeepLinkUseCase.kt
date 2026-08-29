package com.poti.android.domain.usecase.auth

import com.poti.android.domain.manager.AuthSessionManager
import javax.inject.Inject

class SetPendingReturnDeepLinkUseCase @Inject constructor(
    private val authSessionManager: AuthSessionManager,
) {
    operator fun invoke(deepLink: String) = authSessionManager.setPendingReturnDeepLink(deepLink)
}
