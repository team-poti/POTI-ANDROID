package com.poti.android.domain.repository

import com.poti.android.domain.model.history.DeliveryDetail

interface DeliveryRepository {
    suspend fun patchDelivery(
        orderId: Long,
        deliveryMethod: String,
        trackingNumber: String,
    ): Result<DeliveryDetail>
}
