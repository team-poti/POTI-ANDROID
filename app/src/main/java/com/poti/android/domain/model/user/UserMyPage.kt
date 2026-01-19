package com.poti.android.domain.model.user

data class UserMyPage(
    val nickname: String,
    val email: String,
    val profileImageUrl: String?,
    val ratingAvg: String,
    val activityMessage: String,
    val joinedAt: String,
    val hasFavoriteArtist: Boolean,
    val favoriteArtistName: String?,
    val participationSummary: HistorySummary,
    val recruitSummary: HistorySummary,
)
