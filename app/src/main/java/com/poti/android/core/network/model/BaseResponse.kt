package com.poti.android.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class ApiBusinessException(message: String) : Exception(message)

class MissingResponseDataException : IllegalStateException("Required response data is missing")

@Serializable
data class BaseResponse<T>(
    @SerialName("code")
    val code: Int,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: T? = null,
)

fun <T> BaseResponse<T>.handleApiResponse(): Result<T> =
    if (this.code in 200..299) {
        this.data?.let { Result.success(it) } ?: Result.failure(MissingResponseDataException())
    } else {
        Result.failure(ApiBusinessException(this.message))
    }

fun <T> BaseResponse<T>.handleNullableApiResponse(): Result<T?> =
    if (this.code in 200..299) {
        Result.success(this.data)
    } else {
        Result.failure(ApiBusinessException(this.message))
    }
