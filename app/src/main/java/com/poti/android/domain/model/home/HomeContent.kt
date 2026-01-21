package com.poti.android.domain.model.home

data class HomeContent(
    val nickname: String = "",
    val mainArtist: String = "",
    val mainArtistId: Long = 0L,
    val myGroupItems: List<GroupItem> = emptyList(),
    val otherGroupItems: List<GroupItem> = emptyList(),
    val banners: List<Banner> = emptyList(),
)

data class GroupItem(
    val postTitle: String,
    val artist: String,
    val artistId: Long,
    val postImage: String,
    val postCount: Int,
    val tag: String,
)

data class Banner(
    val postId: Long,
    val imageUrl: String,
)
