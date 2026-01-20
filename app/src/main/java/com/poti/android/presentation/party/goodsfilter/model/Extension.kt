package com.poti.android.presentation.party.goodsfilter.model

import com.poti.android.domain.model.party.PartySummary
import java.text.NumberFormat
import java.util.Locale

val PartySummary.priceText: String
    get() = "${NumberFormat.getNumberInstance(Locale.KOREA).format(price)}원~"

val PartySummary.ratingText: String
    get() = String.format(Locale.KOREA, "%.2f", rating)

val PartySummary.membersText: String
    get() = availableMembers.joinToString(" | ") { it.name }
