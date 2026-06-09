package com.poti.android.domain.usecase.history

import com.poti.android.domain.model.history.ParticipantPaymentConfirm
import com.poti.android.domain.repository.PaymentRepository
import javax.inject.Inject

class ConfirmPaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository,
) {
    suspend operator fun invoke(orderId: Long): Result<ParticipantPaymentConfirm> =
        paymentRepository.patchPaymentConfirm(orderId)
}
