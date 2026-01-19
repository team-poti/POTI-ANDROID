package com.poti.android.data.remote.dto.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OnboardingResponseDto(
    @SerialName("nickname")
    val nickname: String,
    @SerialName("favoriteArtistId")
    val favoriteArtistId: Long,
)
