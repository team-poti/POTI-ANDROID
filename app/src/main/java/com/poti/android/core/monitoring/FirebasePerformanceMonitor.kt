package com.poti.android.core.monitoring

import com.google.firebase.perf.FirebasePerformance
import com.poti.android.BuildConfig
import com.poti.android.core.network.model.ApiBusinessException
import com.poti.android.core.network.model.NetworkError
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val MAX_CALLER_ATTRIBUTES = 3
private const val RESULT_ATTRIBUTE = "result"
private const val ERROR_TYPE_ATTRIBUTE = "error_type"
private const val SUCCESS = "success"
private const val FAILURE = "failure"

@Singleton
class FirebasePerformanceMonitor @Inject constructor() : PerformanceMonitor {
    override suspend fun <T> traceResult(
        name: String,
        attributes: Map<String, String>,
        block: suspend () -> Result<T>,
    ): Result<T> {
        if (!BuildConfig.PERFORMANCE_MONITORING_ENABLED) return block()

        val trace = runCatching {
            FirebasePerformance.getInstance().newTrace(name).apply {
                attributes.entries.take(MAX_CALLER_ATTRIBUTES).forEach { (key, value) ->
                    putAttribute(key, value)
                }
                start()
            }
        }.getOrNull() ?: return block()

        return try {
            block().also { result ->
                runCatching {
                    trace.putAttribute(RESULT_ATTRIBUTE, if (result.isSuccess) SUCCESS else FAILURE)
                    result.exceptionOrNull()?.let { error ->
                        trace.putAttribute(ERROR_TYPE_ATTRIBUTE, error.performanceErrorType())
                    }
                }
            }
        } catch (error: Throwable) {
            runCatching {
                trace.putAttribute(RESULT_ATTRIBUTE, FAILURE)
                trace.putAttribute(ERROR_TYPE_ATTRIBUTE, error.performanceErrorType())
            }
            throw error
        } finally {
            runCatching { trace.stop() }
        }
    }
}

private fun Throwable.performanceErrorType(): String = when (this) {
    is NetworkError.NetworkConnection -> "network"
    is NetworkError.ServerError -> "server"
    is NetworkError.BadRequest,
    is NetworkError.Unauthorized,
    is NetworkError.Forbidden,
    is NetworkError.NotFound,
    is NetworkError.Conflict,
    -> "http"
    is ApiBusinessException -> "business"
    is HttpException -> "http"
    is IOException -> "network"
    else -> "unknown"
}

@Module
@InstallIn(SingletonComponent::class)
abstract class PerformanceMonitorModule {
    @Binds
    abstract fun bindPerformanceMonitor(implementation: FirebasePerformanceMonitor): PerformanceMonitor
}
