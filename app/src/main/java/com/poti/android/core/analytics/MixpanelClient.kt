package com.poti.android.core.analytics

import android.content.Context
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.poti.android.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MixpanelClient @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    var instance: MixpanelAPI? = null
        private set

    fun initialize() {
        if (!BuildConfig.MIXPANEL_ENABLED || instance != null) return

        instance = MixpanelAPI.getInstance(context, BuildConfig.MIXPANEL_PROJECT_TOKEN, false)
    }
}
