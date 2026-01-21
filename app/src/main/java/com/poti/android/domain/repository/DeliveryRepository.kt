package com.poti.android.domain.repository

import com.poti.android.data.remote.dto.request.history.DeliveryRequestDto
import com.poti.android.domain.model.history.DeliveryDetail

interface DeliveryRepository {
    suspend fun patchDelivery(orderId: Long, deliveryReq: DeliveryRequestDto): Result<DeliveryDetail>
}
