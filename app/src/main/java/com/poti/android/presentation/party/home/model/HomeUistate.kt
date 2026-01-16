package com.poti.android.presentation.party.home.model

data class HomeUiState(
    val nickname: String = "",
    val mainArtist: String = "",
    val myGroupItems: List<GroupItemUiModel> = emptyList(),
    val otherGroupItems: List<GroupItemUiModel> = emptyList(),
    val banners: List<BannerUiModel> = emptyList(),
)

data class GroupItemUiModel(
    val postTitle: String,
    val artist: String,
    val postImage: String,
    val postCount: Long,
    val tag: String,
)

data class BannerUiModel(
    val postId: Long,
    val imageUrl: String,
)
