package com.poti.android.domain.model.delivery

data class DeliveryOption(
    val deliveryId: Long,
    val name: String, // 배송 방식
    val price: Int, // 배송비
)
