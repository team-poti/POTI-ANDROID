package com.poti.android.domain.model.history

import com.poti.android.presentation.history.component.ParticipantStateLabelStage
import com.poti.android.presentation.history.component.ParticipantStateLabelStatus

data class HistoryListContent(
    val ongoingCount: Int,
    val endedCount: Int,
    val items: List<HistoryItem>,
)

data class HistoryItem(
    val id: Long,
    val imageUrl: String?,
    val artist: String,
    val title: String,
    val stage: ParticipantStateLabelStage,
    val status: ParticipantStateLabelStatus,
)
