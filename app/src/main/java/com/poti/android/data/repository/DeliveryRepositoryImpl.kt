package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.history.toDomain
import com.poti.android.data.remote.dto.request.history.DeliveryRequestDto
import com.poti.android.data.remote.service.DeliveryService
import com.poti.android.domain.model.history.DeliveryDetail
import com.poti.android.domain.repository.DeliveryRepository
import javax.inject.Inject

class DeliveryRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val deliveryService: DeliveryService
): DeliveryRepository {
    override suspend fun patchDelivery(orderId: Long, deliveryReq: DeliveryRequestDto): Result<DeliveryDetail> =
        httpResponseHandler.safeApiCall {
            deliveryService.patchDelivery(orderId, deliveryReq)
                .handleApiResponse()
                .getOrThrow()
                .toDomain()
        }

}
