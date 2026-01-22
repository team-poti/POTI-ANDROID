package com.poti.android.domain.repository

import com.poti.android.domain.model.history.ParticipantPaymentConfirm

interface PaymentRepository {
    suspend fun patchPaymentConfirm(orderId: Long): Result<ParticipantPaymentConfirm>
}
