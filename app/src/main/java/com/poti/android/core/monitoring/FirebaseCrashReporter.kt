package com.poti.android.core.monitoring

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.poti.android.BuildConfig
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseCrashReporter @Inject constructor() : CrashReporter {
    override fun recordNonFatal(
        error: Throwable,
        operation: CrashOperation,
    ) {
        if (BuildConfig.FLAVOR != "prod" || BuildConfig.BUILD_TYPE != "release") return
        if (!isUnexpectedFailure(error)) return
        // Reporting must not alter authentication or UI error handling.
        runCatching {
            FirebaseCrashlytics.getInstance().recordException(sanitizedException(error, operation))
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class CrashReporterModule {
    @Binds
    abstract fun bindCrashReporter(implementation: FirebaseCrashReporter): CrashReporter
}
