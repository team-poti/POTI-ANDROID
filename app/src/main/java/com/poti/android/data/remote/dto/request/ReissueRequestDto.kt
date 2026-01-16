package com.poti.android.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReissueRequestDto(
    @SerialName("refreshToken")
    val refreshToken: String,
)
