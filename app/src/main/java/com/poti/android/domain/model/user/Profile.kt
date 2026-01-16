package com.poti.android.domain.model.user

data class Profile(
    val userId: Long,
    val email: String,
    val nickname: String,
    val profileImageUrl: String,
    val ratingAvg: Double,
    val activityMessage: String,
    val joinedAt: String,
    val hasFavoriteArtist: Boolean,
    val recruitSummary: RecruitSummary,
)

data class RecruitSummary(
    val total: Int,
    val inProgress: Int,
    val completed: Int,
)
