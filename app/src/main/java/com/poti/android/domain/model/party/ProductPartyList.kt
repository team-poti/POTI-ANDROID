package com.poti.android.domain.model.party

data class ProductPartyList(
    val partyTitle: String,
    val artistName: String,
    val partySummaries: List<PartySummary>,
    val currentPage: Int = 0,
    val hasNext: Boolean = false,
)

data class PartySummary(
    val partyId: Long,
    val price: Int,
    val productImageUrl: String,
    val currentCount: Int,
    val totalCount: Int,
    val availableMembers: List<String>,
    val profileImageUrl: String?,
    val nickname: String,
    val rating: Double,
)
