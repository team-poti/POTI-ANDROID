package com.poti.android.presentation.history.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.type.StatusColor
import com.poti.android.domain.type.HistoryListType
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.domain.type.PartyStatusType

val StatusColor.color: Color
    @Composable
    get() = when (this) {
        StatusColor.RED -> PotiTheme.colors.sementicRed
        StatusColor.BLUE -> PotiTheme.colors.poti600
        StatusColor.GRAY -> PotiTheme.colors.gray700
    }

val HistoryListType.statusColor: StatusColor
    get() = when (this) {
        HistoryListType.IN_PROGRESS -> StatusColor.RED
        HistoryListType.COMPLETED -> StatusColor.BLUE
    }

val PartyStatusType.statusColor: StatusColor
    get() = when (this) {
        PartyStatusType.RECRUITING -> StatusColor.RED
        PartyStatusType.CLOSED -> StatusColor.BLUE
        PartyStatusType.PAYMENT_DONE -> StatusColor.BLUE
        PartyStatusType.SHIPPING -> StatusColor.BLUE
        PartyStatusType.DELIVERED -> StatusColor.GRAY
        PartyStatusType.COMPLETED -> StatusColor.GRAY
    }

val ParticipantStatusType.statusColor: StatusColor
    get() = when (this) {
        ParticipantStatusType.RECRUITING -> StatusColor.RED
        ParticipantStatusType.WAIT_PAY -> StatusColor.GRAY
        ParticipantStatusType.WAIT_PAY_CHECK -> StatusColor.RED
        ParticipantStatusType.PAID -> StatusColor.BLUE
        ParticipantStatusType.READY -> StatusColor.RED
        ParticipantStatusType.SHIPPED -> StatusColor.BLUE
        ParticipantStatusType.DELIVERED -> StatusColor.GRAY
    }
