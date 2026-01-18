package com.poti.android.domain.model.party

data class Pots(
    val postTitle: String,
    val artistName: String,
    val potSummaries: List<PotSummary>
)

data class PotSummary(
    val potId: Long,
    val price: String,
    val goodsImageUrl: String,
    val currentCount: Int,
    val totalCount: Int,
    val availableMembers: String,
    val profileImageUrl: String?,
    val nickname: String,
    val rating: String,
)
