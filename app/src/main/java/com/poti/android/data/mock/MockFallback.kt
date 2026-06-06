package com.poti.android.data.mock

import com.poti.android.BuildConfig
import timber.log.Timber

suspend inline fun <T> Result<T>.useUiMockWhenEnabled(
    useMock: Boolean = BuildConfig.USE_UI_MOCK,
    crossinline mock: suspend () -> T,
): Result<T> {
    if (!useMock) return this

    Timber.d("UI mock build enabled. Returning mock data.")
    return runCatching { mock() }
}
