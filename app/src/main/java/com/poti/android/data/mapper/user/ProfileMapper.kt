package com.poti.android.data.mapper.user

import com.poti.android.data.remote.dto.response.user.ProfileResponseDto
import com.poti.android.data.remote.dto.response.user.ProfileSummaryDto
import com.poti.android.domain.model.user.HistorySummary
import com.poti.android.domain.model.user.UserProfile

fun ProfileResponseDto.toDomain(): UserProfile =
    UserProfile(
        userId = userId,
        email = email,
        nickname = nickname,
        profileImageUrl = profileImageUrl,
        ratingAvg = ratingAvg,
        activityMessage = activityMessage,
        joinedAt = joinedAt,
        hasFavoriteArtist = hasFavoriteArtist,
        recruitSummary = recruitSummary.toDomain(),
    )

fun ProfileSummaryDto.toDomain(): HistorySummary =
    HistorySummary(
        total = total,
        inProgress = inProgress,
        completed = completed,
    )
