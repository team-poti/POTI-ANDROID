package com.poti.android.domain.model.history

data class DeliveryDetail(
    val orderId: Long,
    // TODO: [천민재] 로깅용 domain model 이라 enum X
    val deliveryStatus: String,
    val trackingNumber: String,
    val shippedAt: String
)
