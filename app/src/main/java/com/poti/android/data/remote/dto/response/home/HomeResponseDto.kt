package com.poti.android.data.remote.dto.response.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeResponseDto(
    @SerialName("nickname")
    val nickname: String,
    @SerialName("mainArtist")
    val mainArtist: String?,
    @SerialName("myGroupItems")
    val myGroupItems: List<MyGroupItemDto>?,
    @SerialName("otherGroupItems")
    val otherGroupItems: List<OtherGroupItemDto>,
    @SerialName("banners")
    val banners: List<HomeBannerDto>,
)

@Serializable
data class MyGroupItemDto(
    @SerialName("artist")
    val artist: String?,
    @SerialName("postImage")
    val postImage: String?,
    @SerialName("postTitle")
    val postTitle: String?,
    @SerialName("postCount")
    val postCount: Int?,
    @SerialName("tag")
    val tag: String?,
)

@Serializable
data class OtherGroupItemDto(
    @SerialName("artist")
    val artist: String,
    @SerialName("postImage")
    val postImage: String?,
    @SerialName("postTitle")
    val postTitle: String,
    @SerialName("postCount")
    val postCount: Int,
    @SerialName("tag")
    val tag: String,
)

@Serializable
data class HomeBannerDto(
    @SerialName("postId")
    val postId: Long,
    @SerialName("imageUrl")
    val imageUrl: String,
)
