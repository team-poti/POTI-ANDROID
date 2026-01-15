package com.poti.android.domain.model

import androidx.annotation.StringRes
import com.poti.android.presentation.user.component.HistorySummaryType

data class HistorySummaryItem(
    val type: HistorySummaryType,
    @StringRes val titleRes: Int,
    val count: Int,
)
