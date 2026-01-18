package com.poti.android.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("code")
    val code: Int,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: T?,
)

fun <T> BaseResponse<T>.handleApiResponse(): Result<T> =
    if (this.code in 200..299) {
        this.data?.let { Result.success(it) } ?: Result.failure(Exception("Response success but data is null"))
    } else {
        Result.failure(Exception(this.message))
    }

fun <T> BaseResponse<T>.handleNullableApiResponse(): Result<T?> =
    if (this.code in 200..299) {
        Result.success(this.data)
    } else {
        Result.failure(Exception(this.message))
    }
