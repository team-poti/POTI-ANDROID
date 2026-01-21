package com.poti.android.presentation.history.mapper

import androidx.annotation.StringRes
import com.poti.android.R
import com.poti.android.domain.type.HistoryListType
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.domain.type.PartyStatusType

val HistoryListType.labelResId: Int
    @StringRes get() = when (this) {
        HistoryListType.IN_PROGRESS -> R.string.party_status_recruiting
        HistoryListType.COMPLETED -> R.string.party_status_closed
    }

val PartyStatusType.labelResId: Int
    @StringRes get() = when (this) {
        PartyStatusType.RECRUITING -> R.string.party_status_recruiting
        PartyStatusType.CLOSED -> R.string.party_status_closed
        PartyStatusType.PAYMENT_DONE -> R.string.party_status_payment_done
        PartyStatusType.SHIPPING -> R.string.party_status_shipping
        PartyStatusType.DELIVERED -> R.string.party_status_delivered
        PartyStatusType.COMPLETED -> R.string.party_status_delivered
    }

val ParticipantStatusType.labelResId: Int
    @StringRes get() = when (this) {
        ParticipantStatusType.RECRUITING -> R.string.party_status_recruiting
        ParticipantStatusType.WAIT_PAY -> R.string.participant_status_wait_pay
        ParticipantStatusType.WAIT_PAY_CHECK -> R.string.participant_status_wait_pay_check
        ParticipantStatusType.PAID -> R.string.party_status_payment_done
        ParticipantStatusType.READY -> R.string.participant_status_ready
        ParticipantStatusType.SHIPPED -> R.string.party_status_shipping
        ParticipantStatusType.DELIVERED -> R.string.party_status_delivered
    }
