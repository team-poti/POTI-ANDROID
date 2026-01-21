package com.poti.android.data.remote.dto.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticipationSummaryDto(
    @SerialName("total")
    val total: Int,
    @SerialName("inProgress")
    val inProgress: Int,
    @SerialName("completed")
    val completed: Int,
)

@Serializable
data class RecruitSummaryDto(
    @SerialName("total")
    val total: Int,
    @SerialName("inProgress")
    val inProgress: Int,
    @SerialName("completed")
    val completed: Int,
)
