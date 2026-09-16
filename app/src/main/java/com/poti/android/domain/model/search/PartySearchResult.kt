package com.poti.android.domain.model.search

data class PartySearchResult(
    val items: List<PartySearchItem>,
    val hasNext: Boolean,
)

data class PartySearchItem(
    val artist: String,
    val artistId: Long,
    val postImage: String,
    val postTitle: String,
    val postCount: Long,
    val tag: String?,
)
