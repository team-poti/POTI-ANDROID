package com.poti.android.domain.model.party

data class GoodsCategory(
    val nickname: String,
    val mainArtist: String?,
    val groupItems: List<GroupItem>,
)

data class GroupItem(
    val artist: String,
    val postImage: String,
    val postTitle: String,
    val postCount: Int,
    val tag: String?,
)
