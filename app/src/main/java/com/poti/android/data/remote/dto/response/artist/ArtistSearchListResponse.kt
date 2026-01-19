package com.poti.android.data.remote.dto.response.artist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistSearchListResponseDto(
    @SerialName("artists")
    val artists: List<ArtistSearchResponseDto>,
)

@Serializable
data class ArtistSearchResponseDto(
    @SerialName("artists")
    val artists: Long,
    @SerialName("name")
    val name: String,
)
