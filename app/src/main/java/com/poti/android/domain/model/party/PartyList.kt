package com.poti.android.domain.model.party

import java.text.NumberFormat
import java.util.Locale

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
    val availableMembers: String,
    val profileImageUrl: String?,
    val nickname: String,
    val rating: Double,
) {
    val priceText = "${NumberFormat.getNumberInstance(Locale.KOREA).format(price)}원~"
    val ratingText = String.format(Locale.KOREA, "%.2f", rating)
}
