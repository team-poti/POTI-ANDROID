package com.poti.android.domain.repository

import com.poti.android.domain.model.payment.PaymentResult

interface PaymentRepository {
    suspend fun postPayment(
        orderId: Long,
        depositorName: String,
        depositAt: String,
    ): Result<PaymentResult>
}
