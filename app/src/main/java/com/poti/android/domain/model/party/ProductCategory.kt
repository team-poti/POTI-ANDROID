package com.poti.android.domain.model.party

data class ProductCategory(
    val nickname: String,
    val mainArtist: String?,
    val mainArtistId: Long?,
    val groupItems: List<GroupItem>,
)

data class GroupItem(
    val artist: String,
    val artistId: Long,
    val postImage: String,
    val postTitle: String,
    val postCount: Int,
    val tag: String?,
)
