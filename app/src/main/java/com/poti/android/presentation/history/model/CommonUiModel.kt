package com.poti.android.presentation.history.model

import com.poti.android.presentation.history.component.ParticipantStateLabelStage
import com.poti.android.presentation.history.component.ParticipantStateLabelStatus

data class PartySummaryUiModel(
    val imageUrl: String,
    val artist: String,
    val title: String,
    val partyStage: ParticipantStateLabelStage,
    val partyStatus: ParticipantStateLabelStatus,
)

data class ProgressUiModel(
    val guideText: String,
    val step: Int,
)
