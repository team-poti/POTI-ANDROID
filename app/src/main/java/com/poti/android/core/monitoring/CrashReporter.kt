package com.poti.android.core.monitoring

import com.poti.android.core.network.model.ApiBusinessException
import com.poti.android.core.network.model.NetworkError
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

interface CrashReporter {
    fun recordNonFatal(
        error: Throwable,
        operation: CrashOperation,
    )
}

enum class CrashOperation {
    API_RESPONSE,
    VIEW_MODEL,
    TOKEN_REISSUE,
    TOKEN_RESPONSE,
    TOKEN_PERSIST,
    TOKEN_CLEAR,
}

fun isUnexpectedFailure(error: Throwable): Boolean =
    error !is CancellationException && error !is IOException &&
        error !is HttpException && error !is NetworkError && error !is ApiBusinessException

// Never retain messages, causes or suppressed exceptions: parsers can include response bodies.
fun sanitizedException(
    error: Throwable,
    operation: CrashOperation,
): Throwable =
    RuntimeException("${operation.name}: ${error.javaClass.simpleName}").apply {
        stackTrace = error.stackTrace.copyOf()
    }
