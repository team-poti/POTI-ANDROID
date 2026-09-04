package com.poti.android.domain.model.user

data class UserProfile(
    val userId: Long,
    val nickname: String,
    val profileImageUrl: String?,
    val ratingAvg: Double,
    val activityMessage: String,
    val joinedAt: String,
    val participationSummary: HistorySummary,
    val recruitSummary: HistorySummary,
)

data class HistorySummary(
    val inProgress: Int,
    val completed: Int,
)
