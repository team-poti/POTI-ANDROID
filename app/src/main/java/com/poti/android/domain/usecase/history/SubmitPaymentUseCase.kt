package com.poti.android.domain.usecase.history

import com.poti.android.domain.model.payment.PaymentResult
import com.poti.android.domain.repository.PaymentRepository
import javax.inject.Inject

class SubmitPaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository,
) {
    suspend operator fun invoke(
        orderId: Long,
        depositorName: String,
        depositAt: String,
    ): Result<PaymentResult> = paymentRepository.postPayment(orderId, depositorName, depositAt)
}
