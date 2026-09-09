package com.poti.android.core.analytics

interface EventTracker {
    fun track(
        eventName: String,
        properties: Map<String, Any?> = emptyMap(),
    )

    fun identify(userId: String)

    fun setUserProperties(properties: Map<String, Any?>)

    fun reset()
}
