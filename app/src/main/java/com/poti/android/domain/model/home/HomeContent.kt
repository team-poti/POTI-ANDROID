package com.poti.android.domain.model.home

data class HomeContent(
    val nickname: String,
    val mainArtist: String?,
    val mainArtistId: Long?,
    val myGroupItems: List<GroupItem>,
    val otherGroupItems: List<GroupItem>,
    val banners: List<Banner>,
)

data class GroupItem(
    val goodsId: Long,
    val postTitle: String,
    val artist: String,
    val artistId: Long,
    val postImage: String,
    val postCount: Int,
    val tag: String,
)

data class Banner(
    val id: Long,
    val imageUrl: String,
    val deepLink: String,
)
