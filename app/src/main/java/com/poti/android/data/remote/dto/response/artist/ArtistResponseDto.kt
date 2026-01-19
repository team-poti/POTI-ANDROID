package com.poti.android.data.remote.dto.response.artist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistListResponseDto(
    @SerialName("artists")
    val artists: List<ArtistResponseDto>,
)

@Serializable
data class ArtistResponseDto(
    @SerialName("artistId")
    val artistId: Long,
    @SerialName("name")
    val name: String,
    @SerialName("logoImageUrl")
    val logoImageUrl: String?,
)
