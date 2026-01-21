package com.poti.android.data.remote.datasource

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.history.DeliveryRequestDto
import com.poti.android.data.remote.dto.response.history.DeliveryResponseDto
import com.poti.android.data.remote.service.DeliveryService
import javax.inject.Inject

class DeliveryRemoteDataSource @Inject constructor(
    private val deliveryService: DeliveryService
) {
    suspend fun fetchDelivery(orderId: Long, deliveryReq: DeliveryRequestDto): BaseResponse<DeliveryResponseDto> =
        deliveryService.patchDelivery(orderId, deliveryReq)
}
