package com.poti.android.core.analytics

import android.content.Context
import android.os.SystemClock
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppOpenTracker @Inject constructor(
    @ApplicationContext context: Context,
    private val eventTracker: EventTracker,
) : DefaultLifecycleObserver {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private var entryPoint = AnalyticsValue.DIRECT
    private var hasTrackedInProcess = false
    private var isProcessResumed = false
    private var backgroundStartedAtMillis: Long? = null

    fun start() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun setEntryPoint(entryPoint: String) {
        if (
            entryPoint != AnalyticsValue.DIRECT &&
            isProcessResumed &&
            hasTrackedInProcess
        ) {
            trackAppOpened(entryPoint)
            this.entryPoint = AnalyticsValue.DIRECT
            return
        }

        this.entryPoint = entryPoint
    }

    override fun onResume(owner: LifecycleOwner) {
        isProcessResumed = true
        val backgroundDurationMillis = backgroundStartedAtMillis?.let { startedAt ->
            SystemClock.elapsedRealtime() - startedAt
        }

        if (shouldTrackAppOpen(hasTrackedInProcess, entryPoint, backgroundDurationMillis)) {
            trackAppOpened(entryPoint)
        }

        entryPoint = AnalyticsValue.DIRECT
        backgroundStartedAtMillis = null
    }

    override fun onPause(owner: LifecycleOwner) {
        isProcessResumed = false
    }

    override fun onStop(owner: LifecycleOwner) {
        backgroundStartedAtMillis = SystemClock.elapsedRealtime()
    }

    private fun trackAppOpened(entryPoint: String) {
        val isFirstOpen = !preferences.getBoolean(HAS_OPENED_KEY, false)

        eventTracker.track(
            eventName = AnalyticsEvent.APP_OPENED,
            properties = mapOf(
                AnalyticsEventProperty.ENTRY_POINT to entryPoint,
                AnalyticsEventProperty.IS_FIRST_OPEN to isFirstOpen,
            ),
        )

        if (isFirstOpen) {
            preferences.edit().putBoolean(HAS_OPENED_KEY, true).apply()
        }

        hasTrackedInProcess = true
    }

    private companion object {
        const val PREFERENCES_NAME = "analytics_preferences"
        const val HAS_OPENED_KEY = "has_opened"
    }
}

internal const val APP_OPEN_SESSION_TIMEOUT_MILLIS = 30 * 60 * 1_000L

internal fun shouldTrackAppOpen(
    hasTrackedInProcess: Boolean,
    entryPoint: String,
    backgroundDurationMillis: Long?,
): Boolean =
    !hasTrackedInProcess ||
        entryPoint == AnalyticsValue.NOTIFICATION ||
        entryPoint == AnalyticsValue.DEEP_LINK ||
        (backgroundDurationMillis ?: 0L) >= APP_OPEN_SESSION_TIMEOUT_MILLIS
