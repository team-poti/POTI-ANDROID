package com.poti.android.domain.model.delivery

data class DeliveryOption(
    val deliveryId: Long, // 바송 방식 ID
    val name: String, // 배송 방식
    val price: Int, // 배송비
)
