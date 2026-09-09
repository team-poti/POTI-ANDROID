package com.poti.android.core.analytics

import android.content.Context
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.poti.android.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MixpanelClient @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private var instance: MixpanelAPI? = null

    fun initialize() {
        if (!BuildConfig.MIXPANEL_ENABLED || instance != null) return

        runCatching {
            instance = MixpanelAPI.getInstance(context, BuildConfig.MIXPANEL_PROJECT_TOKEN, false)
        }.onFailure {
            Timber.w(it, "Mixpanel initialization failed")
        }
    }

    internal fun execute(action: (MixpanelAPI) -> Unit) {
        instance?.let(action)
    }
}
