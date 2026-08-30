package com.poti.android.presentation.main

import com.poti.android.domain.model.auth.AuthState
import org.junit.Assert.assertEquals
import org.junit.Test

class MainViewModelTest {
    @Test
    fun `deep link entry mode follows authentication and onboarding state`() {
        assertEquals(
            DeepLinkEntryMode.DEFER,
            AuthState(
                accessToken = null,
                isOnboardingFinished = false,
            ).toDeepLinkEntryMode(),
        )
        assertEquals(
            DeepLinkEntryMode.DEFER,
            AuthState(
                accessToken = "access-token",
                isOnboardingFinished = false,
                isInitialized = true,
            ).toDeepLinkEntryMode(),
        )
        assertEquals(
            DeepLinkEntryMode.MEMBER,
            AuthState(
                accessToken = "access-token",
                isOnboardingFinished = true,
                isInitialized = true,
            ).toDeepLinkEntryMode(),
        )
        assertEquals(
            DeepLinkEntryMode.GUEST,
            AuthState(
                accessToken = null,
                isOnboardingFinished = false,
                isInitialized = true,
            ).toDeepLinkEntryMode(),
        )
    }
}
