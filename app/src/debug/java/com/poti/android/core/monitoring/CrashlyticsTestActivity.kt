package com.poti.android.core.monitoring

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.crashlytics.FirebaseCrashlytics

/** Debug-only entry point for an end-to-end Crashlytics test from adb. */
class CrashlyticsTestActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.getBooleanExtra(EXTRA_DISABLE_COLLECTION, false)) {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)
            finish()
            return
        }

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        Handler(Looper.getMainLooper()).postDelayed(
            { throw IllegalStateException(TEST_CRASH_MESSAGE) },
            CRASH_DELAY_MILLIS,
        )
    }

    private companion object {
        const val TEST_CRASH_MESSAGE = "POTI Crashlytics QA crash"
        const val CRASH_DELAY_MILLIS = 1_000L
        const val EXTRA_DISABLE_COLLECTION = "disable_collection"
    }
}
