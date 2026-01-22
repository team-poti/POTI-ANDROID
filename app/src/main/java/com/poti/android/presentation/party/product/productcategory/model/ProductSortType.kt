package com.poti.android.presentation.party.product.productcategory.model

import androidx.annotation.StringRes
import com.poti.android.R

enum class ProductSortType(
    val request: String,
    @StringRes val displayRes: Int,
) {
    LATEST(
        request = "LATEST",
        displayRes = R.string.party_filter_sort_latest,
    ),
    HOT(
        request = "HOT",
        displayRes = R.string.party_filter_sort_hot,
    ),
}
