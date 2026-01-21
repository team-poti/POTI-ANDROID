package com.poti.android.domain.model.history

data class ParticipantPaymentConfirm(
    val orderId: Long,
    // TODO: [천민재] 로깅용 domain model 이라 따로 enum 처리 안함
    val orderStatus: String,
    val confirmedAt: String
)
