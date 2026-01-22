package com.poti.android.presentation.party.product.partylist.model

import androidx.annotation.StringRes
import com.poti.android.R

enum class PartySortType(
    val request: String,
    @StringRes val displayRes: Int,
) {
    LATEST("LATEST", R.string.party_filter_sort_latest),
    DEADLINE("DEADLINE", R.string.party_filter_sort_deadline),
    RATING("RATING", R.string.party_filter_sort_rating),
}
