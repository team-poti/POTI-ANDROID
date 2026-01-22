package com.poti.android.data.remote.dto.response.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoodsCategoryResponseDto(
    @SerialName("nickname")
    val nickname: String,
    @SerialName("mainArtist")
    val mainArtist: String?,
    @SerialName("mainArtistId")
    val mainArtistId: Long?,
    @SerialName("groupItems")
    val groupItems: List<GroupItemDto>?,
    @SerialName("myGroupItems")
    val myGroupItems: List<GroupItemDto>?,
)

@Serializable
data class GroupItemDto(
    @SerialName("artist")
    val artist: String?,
    @SerialName("artistId")
    val artistId: Long?,
    @SerialName("postImage")
    val postImage: String?,
    @SerialName("postTitle")
    val postTitle: String?,
    @SerialName("postCount")
    val postCount: Int?,
    @SerialName("tag")
    val tag: String?,
)
