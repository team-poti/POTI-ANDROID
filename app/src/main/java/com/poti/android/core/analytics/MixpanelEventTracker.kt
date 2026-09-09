package com.poti.android.core.analytics

import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MixpanelEventTracker @Inject constructor(
    private val mixpanelClient: MixpanelClient,
) : EventTracker {
    override fun track(
        eventName: String,
        properties: Map<String, Any>,
    ) {
        if (eventName.isBlank()) return

        executeSafely("track") { mixpanel ->
            mixpanel.track(eventName, JSONObject(properties))
        }
    }

    override fun identify(userId: String) {
        if (userId.isBlank()) return

        executeSafely("identify") { mixpanel ->
            mixpanel.identify(userId)
        }
    }

    override fun setUserProperties(properties: Map<String, Any>) {
        if (properties.isEmpty()) return

        executeSafely("set_user_properties") { mixpanel ->
            mixpanel.people.set(JSONObject(properties))
        }
    }

    override fun reset() {
        executeSafely("reset") { mixpanel ->
            mixpanel.reset()
        }
    }

    private fun executeSafely(
        operation: String,
        action: (MixpanelAPI) -> Unit,
    ) {
        mixpanelClient.execute { mixpanel ->
            runCatching { action(mixpanel) }
                .onFailure { Timber.w(it, "Mixpanel operation failed: %s", operation) }
        }
    }
}
