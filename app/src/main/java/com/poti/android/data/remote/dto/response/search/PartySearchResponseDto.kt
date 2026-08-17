package com.poti.android.data.remote.dto.response.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PartySearchResponseDto(
    @SerialName("content")
    val content: List<PartySearchItemResponseDto>,
    @SerialName("hasNext")
    val hasNext: Boolean,
)

@Serializable
data class PartySearchItemResponseDto(
    @SerialName("artist")
    val artist: String,
    @SerialName("artistId")
    val artistId: Long,
    @SerialName("postImage")
    val postImage: String?,
    @SerialName("postTitle")
    val postTitle: String,
    @SerialName("postCount")
    val postCount: Long,
    @SerialName("tag")
    val tag: String?,
)
