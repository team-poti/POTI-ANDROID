package com.poti.android.core.analytics

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppOpenTracker @Inject constructor(
    @ApplicationContext context: Context,
    private val eventTracker: EventTracker,
) {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun track(entryPoint: String) {
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
    }

    private companion object {
        const val PREFERENCES_NAME = "analytics_preferences"
        const val HAS_OPENED_KEY = "has_opened"
    }
}
