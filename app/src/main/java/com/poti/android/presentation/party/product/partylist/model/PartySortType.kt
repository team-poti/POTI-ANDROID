package com.poti.android.presentation.party.product.partylist.model

import androidx.annotation.StringRes
import com.poti.android.R
import com.poti.android.core.analytics.AnalyticsValue

enum class PartySortType(
    val request: String,
    @StringRes val displayRes: Int,
) {
    LATEST("LATEST", R.string.party_filter_sort_latest),
    DEADLINE("DEADLINE", R.string.party_filter_sort_deadline),
    RATING("RATING", R.string.party_filter_sort_rating),

    ;

    val analyticsValue: String
        get() = when (this) {
            LATEST -> AnalyticsValue.LATEST
            DEADLINE -> AnalyticsValue.DEADLINE
            RATING -> AnalyticsValue.RATING
        }
}
