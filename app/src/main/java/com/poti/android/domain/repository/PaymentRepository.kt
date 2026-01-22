package com.poti.android.domain.repository

import com.poti.android.domain.model.history.ParticipantPaymentConfirm
import com.poti.android.domain.model.payment.PaymentResult

interface PaymentRepository {
    suspend fun patchPaymentConfirm(orderId: Long): Result<ParticipantPaymentConfirm>

    suspend fun postPayment(
        orderId: Long,
        depositorName: String,
        depositAt: String,
    ): Result<PaymentResult>
}
