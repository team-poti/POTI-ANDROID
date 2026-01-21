package com.poti.android.data.remote.dto.response.party

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PartyListResponseDto(
    @SerialName("postTitle")
    val postTitle: String,
    @SerialName("artistId")
    val artistId: Long,
    @SerialName("artist")
    val artist: String,
    @SerialName("totalCount")
    val totalCount: Int? = null,
    @SerialName("hasNext")
    val hasNext: Boolean,
    @SerialName("pots")
    val pots: List<PartyDto>,
)

@Serializable
data class PartyDto(
    @SerialName("potId")
    val potId: Long,
    @SerialName("price")
    val price: Int,
    @SerialName("thumbnailUrl")
    val thumbnailUrl: String?,
    @SerialName("currentCount")
    val currentCount: Int,
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("status")
    val status: String,
    @SerialName("availableMembers")
    val availableMembers: List<String>,
    @SerialName("uploader")
    val uploader: PartyUploaderDto,
)

@Serializable
data class PartyUploaderDto(
    @SerialName("userId")
    val userId: Long,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("profileImage")
    val profileImage: String?,
    @SerialName("rating")
    val rating: Double,
)
