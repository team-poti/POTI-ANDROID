package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.payment.PaymentRequestDto
import com.poti.android.data.remote.dto.response.history.ParticipantPaymentConfirmResponseDto
import com.poti.android.data.remote.dto.response.payment.PaymentResponseDto
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentService {
    @POST("/api/v1/payments")
    suspend fun postPayment(
        @Body paymentReq: PaymentRequestDto,
    ): BaseResponse<PaymentResponseDto>

    @PATCH("/api/v1/payments/{orderId}/confirm")
    suspend fun patchPaymentConfirm(
        @Path("orderId") orderId: Long,
    ): BaseResponse<ParticipantPaymentConfirmResponseDto>
}
