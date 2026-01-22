package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.history.ParticipantPaymentConfirmResponseDto
import retrofit2.http.PATCH
import retrofit2.http.Path

interface PaymentService {
    @PATCH("/api/v1/payments/{orderId}/confirm")
    suspend fun patchPaymentConfirm(
        @Path("orderId") orderId: Long,
    ): BaseResponse<ParticipantPaymentConfirmResponseDto>
}
