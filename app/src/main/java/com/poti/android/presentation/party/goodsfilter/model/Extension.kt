package com.poti.android.presentation.party.goodsfilter.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.poti.android.R
import com.poti.android.domain.model.party.PartySummary
import java.text.NumberFormat
import java.util.Locale

val PartySummary.priceNumberText: String
    get() = NumberFormat.getNumberInstance(Locale.KOREA).format(price)

@Composable
fun PartySummary.priceText(): String =
    stringResource(R.string.goods_filter_price_format, priceNumberText)

@Composable
fun PartySummary.ratingText(): String =
    stringResource(R.string.goods_filter_rating_format, rating)

@Composable
fun PartySummary.membersText(): String =
    availableMembers.joinToString(
        separator = stringResource(R.string.goods_filter_members_separator),
    ) { it }
