package com.poti.android.data.mapper.payment

import com.poti.android.data.remote.dto.response.payment.PaymentResponseDto
import com.poti.android.domain.model.payment.PaymentResult

fun PaymentResponseDto.toDomain(): PaymentResult = PaymentResult(
    paymentId = this.paymentId,
)
