package com.poti.android.presentation.history.model

import com.poti.android.presentation.history.component.StateLabelStage
import com.poti.android.presentation.history.component.StateLabelStatus

data class PartySummaryUiModel(
    val imageUrl: String,
    val artist: String,
    val title: String,
    val partyStage: StateLabelStage,
    val partyStatus: StateLabelStatus,
)

data class ProgressUiModel(
    val guideText: String,
    val step: Int,
)
