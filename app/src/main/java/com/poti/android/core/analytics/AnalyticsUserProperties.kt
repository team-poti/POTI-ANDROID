package com.poti.android.core.analytics

import com.poti.android.BuildConfig

object AnalyticsUserPropertyKey {
    const val USER_ID = "user_id"
    const val PLATFORM = "platform"
    const val APP_VERSION = "app_version"
    const val ONBOARDING_COMPLETED = "onboarding_completed"
    const val FAVORITE_GROUP_ID = "favorite_group_id"
}

internal fun authenticatedUserProperties(
    userId: Long,
    onboardingCompleted: Boolean,
    appVersion: String = BuildConfig.VERSION_NAME,
): Map<String, Any> = mapOf(
    AnalyticsUserPropertyKey.USER_ID to userId.toString(),
    AnalyticsUserPropertyKey.PLATFORM to "android",
    AnalyticsUserPropertyKey.APP_VERSION to appVersion,
    AnalyticsUserPropertyKey.ONBOARDING_COMPLETED to onboardingCompleted,
)
