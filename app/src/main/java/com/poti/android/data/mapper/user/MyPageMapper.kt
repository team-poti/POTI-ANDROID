package com.poti.android.data.mapper.user

import com.poti.android.data.remote.dto.response.user.MyPageResponseDto
import com.poti.android.data.remote.dto.response.user.ParticipationSummaryDto
import com.poti.android.data.remote.dto.response.user.RecruitSummaryDto
import com.poti.android.domain.model.user.HistorySummary
import com.poti.android.domain.model.user.UserMyPage
import java.lang.String.format
import java.util.Locale

fun MyPageResponseDto.toDomain(): UserMyPage =
    UserMyPage(
        nickname = nickname,
        email = email,
        profileImageUrl = profileImageUrl,
        ratingAvg = format(Locale.US, "%.1f", ratingAvg),
        activityMessage = activityMessage,
        joinedAt = joinedAt,
        hasFavoriteArtist = hasFavoriteArtist,
        favoriteArtistName = favoriteArtistName,
        participationSummary = participationSummary.toDomain(),
        recruitSummary = recruitSummary.toDomain(),
    )

fun ParticipationSummaryDto.toDomain(): HistorySummary =
    HistorySummary(
        total = total,
        inProgress = inProgress,
        completed = completed,
    )

fun RecruitSummaryDto.toDomain(): HistorySummary =
    HistorySummary(
        total = total,
        inProgress = inProgress,
        completed = completed,
    )
