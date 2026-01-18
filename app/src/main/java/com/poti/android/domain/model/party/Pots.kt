package com.poti.android.domain.model.party

import java.text.NumberFormat
import java.util.Locale

data class Pots(
    val postTitle: String,
    val artistName: String,
    val potSummaries: List<PotSummary>,
)

data class PotSummary(
    val potId: Long,
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
