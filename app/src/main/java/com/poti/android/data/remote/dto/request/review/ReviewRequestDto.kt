package com.poti.android.data.remote.dto.request.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewRequestDto(
    @SerialName("transactionId")
    val transactionId: Long,
    @SerialName("star")
    val star: Int,
)
