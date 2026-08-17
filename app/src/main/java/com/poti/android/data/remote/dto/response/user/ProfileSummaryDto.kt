package com.poti.android.data.remote.dto.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileSummaryDto(
    @SerialName("inProgress")
    val inProgress: Int,
    @SerialName("completed")
    val completed: Int,
)
