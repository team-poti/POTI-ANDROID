package com.poti.android.core.analytics

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppOpenTrackerTest {
    @Test
    fun `sets the initial entry point before the first app open in a process`() {
        assertTrue(shouldSetInitialEntryPoint(hasTrackedInProcess = false))
    }

    @Test
    fun `does not reset the entry point after an app open was tracked in the same process`() {
        assertFalse(shouldSetInitialEntryPoint(hasTrackedInProcess = true))
    }

    @Test
    fun `tracks the first foreground entry in a process`() {
        assertTrue(
            shouldTrackAppOpen(
                hasTrackedInProcess = false,
                entryPoint = AnalyticsValue.DIRECT,
                backgroundDurationMillis = null,
            ),
        )
    }

    @Test
    fun `does not track a direct return before session timeout`() {
        assertFalse(
            shouldTrackAppOpen(
                hasTrackedInProcess = true,
                entryPoint = AnalyticsValue.DIRECT,
                backgroundDurationMillis = APP_OPEN_SESSION_TIMEOUT_MILLIS - 1,
            ),
        )
    }

    @Test
    fun `tracks a direct return after session timeout`() {
        assertTrue(
            shouldTrackAppOpen(
                hasTrackedInProcess = true,
                entryPoint = AnalyticsValue.DIRECT,
                backgroundDurationMillis = APP_OPEN_SESSION_TIMEOUT_MILLIS,
            ),
        )
    }

    @Test
    fun `tracks notification and deep link entries before session timeout`() {
        assertTrue(
            shouldTrackAppOpen(
                hasTrackedInProcess = true,
                entryPoint = AnalyticsValue.NOTIFICATION,
                backgroundDurationMillis = 1_000,
            ),
        )
        assertTrue(
            shouldTrackAppOpen(
                hasTrackedInProcess = true,
                entryPoint = AnalyticsValue.DEEP_LINK,
                backgroundDurationMillis = 1_000,
            ),
        )
    }
}
