package com.poti.android.domain.model.home

data class HomeContent(
    val nickname: String = "",
    val mainArtist: String = "",
    val myGroupItems: List<GroupItem> = emptyList(),
    val otherGroupItems: List<GroupItem> = emptyList(),
    val banners: List<Banner> = emptyList(),
)

data class GroupItem(
    val postTitle: String,
    val artist: String,
    val postImage: String,
    val postCount: Long,
    val tag: String,
)

data class Banner(
    val postId: Long,
    val imageUrl: String,
)
