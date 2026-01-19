package com.poti.android.data.remote.dto.response.image

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresignedUrlListResponseDto(
    @SerialName("urls")
    val urls: List<PresignedUrlResponseDto>,
)

@Serializable
data class PresignedUrlResponseDto(
    @SerialName("fileName")
    val fileName: String,
    @SerialName("url")
    val url: String,
)
