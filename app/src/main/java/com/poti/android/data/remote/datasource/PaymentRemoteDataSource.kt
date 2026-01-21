package com.poti.android.data.remote.datasource

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.history.ParticipantPaymentConfirmResponseDto
import com.poti.android.data.remote.service.PaymentService
import javax.inject.Inject

class PaymentRemoteDataSource @Inject constructor(
    private val paymentService: PaymentService
) {
    suspend fun patchPaymentConfirm(orderId: Long): BaseResponse<ParticipantPaymentConfirmResponseDto> =
        paymentService.patchPaymentConfirm(orderId)

}
