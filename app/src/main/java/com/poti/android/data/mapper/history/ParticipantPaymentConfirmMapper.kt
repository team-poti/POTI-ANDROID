package com.poti.android.data.mapper.history

import com.poti.android.data.remote.dto.response.history.ParticipantPaymentConfirmResponseDto
import com.poti.android.domain.model.history.ParticipantPaymentConfirm

fun ParticipantPaymentConfirmResponseDto.toDomain(): ParticipantPaymentConfirm = ParticipantPaymentConfirm(
    orderId = this.orderId,
    orderStatus = this.status,
    confirmedAt = this.confirmedAt
)
