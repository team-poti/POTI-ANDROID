package com.poti.android.data.remote.dto.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponseDto(
    @SerialName("userId")
    val userId: Long,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("profileImageUrl")
    val profileImageUrl: String,
    @SerialName("ratingAvg")
    val ratingAvg: Double,
    @SerialName("activityMessage")
    val activityMessage: String,
    @SerialName("joinedAt")
    val joinedAt: String,
    @SerialName("participationSummary")
    val participationSummary: ProfileSummaryDto,
    @SerialName("recruitSummary")
    val recruitSummary: ProfileSummaryDto,
)
