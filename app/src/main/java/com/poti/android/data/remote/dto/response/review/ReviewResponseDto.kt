package com.poti.android.data.remote.dto.response.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponseDto(
    @SerialName("reviewId")
    val reviewId: Long
)
