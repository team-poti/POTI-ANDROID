package com.poti.android.domain.model.user

data class UserMyPage(
    val userId: Long,
    val nickname: String,
    val email: String,
    val profileImageUrl: String,
    val ratingAvg: Double,
    val activityMessage: String,
    val joinedAt: String,
    val hasFavoriteArtist: Boolean,
    val favoriteArtistName: String,
    val participationSummary: HistorySummary,
    val recruitSummary: HistorySummary,
)
