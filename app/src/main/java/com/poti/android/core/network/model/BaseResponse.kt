package com.poti.android.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("status")
    val status: Int,
    @SerialName("code")
    val code: String? = null,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: T?,
)
