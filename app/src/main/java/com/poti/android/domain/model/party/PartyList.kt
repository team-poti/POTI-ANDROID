package com.poti.android.domain.model.party

import com.poti.android.domain.model.artist.Member

data class PartyList(
    val partyTitle: String,
    val artistName: String,
    val partySummaries: List<PartySummary>,
)

data class PartySummary(
    val partyId: Long,
    val price: Int,
    val goodsImageUrl: String,
    val currentCount: Int,
    val totalCount: Int,
    val availableMembers: List<Member>,
    val profileImageUrl: String?,
    val nickname: String,
    val rating: Double,
)
