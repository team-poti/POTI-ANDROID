package com.poti.android.data.remote.dto.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteArtistRequestDto(
    @SerialName("artistId")
    val artistId: Long,
)
