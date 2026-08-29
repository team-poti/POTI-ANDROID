package com.poti.android.core.network.util

import com.poti.android.BuildConfig
import com.poti.android.core.common.util.suspendRunCatching
import com.poti.android.core.network.model.BaseResponse
import com.poti.android.core.network.model.NetworkError
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HttpResponseHandler @Inject constructor(
    private val json: Json,
) {
    suspend fun <T> safeApiCall(
        block: suspend () -> T,
    ): Result<T> {
        val result = suspendRunCatching { block() }

        return result.recoverCatching { throwable ->
            throw when (throwable) {
                is HttpException -> parseHttpException(throwable)
                is UnknownHostException, is SocketTimeoutException -> NetworkError.NetworkConnection
                is NetworkError -> throwable
                else -> NetworkError.Unknown
            }
        }
    }

    private fun parseHttpException(e: HttpException): NetworkError {
        val errorBody = e.response()?.errorBody()?.string()
        val errorResponse = runCatching {
            errorBody?.let { json.decodeFromString<BaseResponse<Unit>>(it) }
        }.getOrNull()

        val serverMsg = errorResponse?.message ?: e.message()
        val serverCode = errorResponse?.code

        if (BuildConfig.DEBUG) {
            Timber.tag("HttpResponseHandler").d(
                "HTTP error. status=%s, serverCode=%s, message=%s, body=%s",
                e.code(),
                serverCode,
                serverMsg,
                errorBody,
            )
        }

        return when (e.code()) {
            400 -> NetworkError.BadRequest(serverCode, serverMsg)
            401 -> NetworkError.Unauthorized(serverMsg)
            403 -> NetworkError.Forbidden(serverMsg)
            404 -> NetworkError.NotFound(serverMsg)
            409 -> NetworkError.Conflict(serverMsg)
            in 500..599 -> NetworkError.ServerError(serverMsg)
            else -> NetworkError.Unknown
        }
    }
}
