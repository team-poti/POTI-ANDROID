package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.history.DeliveryRequestDto
import com.poti.android.data.remote.dto.response.history.DeliveryResponseDto
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

interface DeliveryService {
    @PATCH("/api/v1/orders/{orderId}/deliveries")
    suspend fun patchDelivery(
        @Path("orderId") orderId: Long,
        @Body deliveryReq: DeliveryRequestDto,
    ): BaseResponse<DeliveryResponseDto>
}
