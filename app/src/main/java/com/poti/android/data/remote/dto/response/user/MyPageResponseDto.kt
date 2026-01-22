package com.poti.android.data.remote.dto.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyPageResponseDto(
    @SerialName("userId")
    val userId: Long,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("email")
    val email: String,
    @SerialName("profileImageUrl")
    val profileImageUrl: String?,
    @SerialName("ratingAvg")
    val ratingAvg: Double,
    @SerialName("activityMessage")
    val activityMessage: String,
    @SerialName("joinedAt")
    val joinedAt: String,
    @SerialName("hasFavoriteArtist")
    val hasFavoriteArtist: Boolean,
    @SerialName("favoriteArtistName")
    val favoriteArtistName: String?,
    @SerialName("participationSummary")
    val participationSummary: ProfileSummaryDto,
    @SerialName("recruitSummary")
    val recruitSummary: ProfileSummaryDto,
)
