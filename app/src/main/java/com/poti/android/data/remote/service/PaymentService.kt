package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.payment.PaymentRequestDto
import com.poti.android.data.remote.dto.response.payment.PaymentResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentService {
    @POST("/api/v1/payments")
    suspend fun postPayment(
        @Body paymentReq: PaymentRequestDto
    ): BaseResponse<PaymentResponseDto>
}
