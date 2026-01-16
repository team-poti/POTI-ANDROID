package com.poti.android.domain.model.user

data class UserProfile(
    val userId: Long,
    val email: String,
    val nickname: String,
    val profileImageUrl: String,
    val ratingAvg: Double,
    val activityMessage: String,
    val joinedAt: String,
    val hasFavoriteArtist: Boolean,
    val recruitSummary: HistorySummary,
)

data class HistorySummary(
    val total: Int,
    val inProgress: Int,
    val completed: Int,
)
