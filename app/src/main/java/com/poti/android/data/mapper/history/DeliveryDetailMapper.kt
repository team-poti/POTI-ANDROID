package com.poti.android.data.mapper.history

import com.poti.android.data.remote.dto.response.history.DeliveryResponseDto
import com.poti.android.domain.model.history.DeliveryDetail

fun DeliveryResponseDto.toDomain(): DeliveryDetail = DeliveryDetail(
    orderId = this.orderId,
    deliveryStatus = this.status,
    trackingNumber = this.trackingNumber,
    shippedTo = this.shippedAt
)
