package com.poti.android.data.remote.dto.response.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeliveryConfirmResponseDto(
    @SerialName("leaderUserId")
    val leaderUserId: Long,
)
