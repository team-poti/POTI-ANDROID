package com.poti.android.data.mock

import com.poti.android.BuildConfig
import com.poti.android.core.common.util.suspendRunCatching
import timber.log.Timber

suspend inline fun <T> Result<T>.useUiMockWhenEnabled(
    useMock: Boolean = BuildConfig.USE_UI_MOCK,
    crossinline mock: suspend () -> T,
): Result<T> {
    if (!useMock) return this

    Timber.d("UI mock build enabled. Returning mock data.")
    return suspendRunCatching { mock() }
}

suspend inline fun <T> executeWithUiMock(
    useMock: Boolean = BuildConfig.USE_UI_MOCK,
    crossinline mock: suspend () -> T,
    crossinline real: suspend () -> Result<T>,
): Result<T> {
    if (!useMock) return real()

    Timber.d("UI mock build enabled. Skipping real write and returning mock data.")
    return suspendRunCatching { mock() }
}
