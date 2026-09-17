package com.poti.android.core.analytics

import org.junit.Assert.assertEquals
import org.junit.Test

class AnalyticsUserPropertiesTest {
    @Test
    fun `authenticated user properties match analytics schema`() {
        val properties = authenticatedUserProperties(
            userId = 42L,
            onboardingCompleted = true,
            appVersion = "1.2.3",
        )

        assertEquals("42", properties[AnalyticsUserPropertyKey.USER_ID])
        assertEquals("android", properties[AnalyticsUserPropertyKey.PLATFORM])
        assertEquals("1.2.3", properties[AnalyticsUserPropertyKey.APP_VERSION])
        assertEquals(true, properties[AnalyticsUserPropertyKey.ONBOARDING_COMPLETED])
    }
}
