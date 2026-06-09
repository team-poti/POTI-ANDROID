package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.history.toDomain
import com.poti.android.data.mock.UiMockData
import com.poti.android.data.mock.executeWithUiMock
import com.poti.android.data.remote.datasource.DeliveryRemoteDataSource
import com.poti.android.data.remote.dto.request.history.DeliveryRequestDto
import com.poti.android.domain.model.history.DeliveryDetail
import com.poti.android.domain.repository.DeliveryRepository
import javax.inject.Inject

class DeliveryRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val deliveryRemoteDataSource: DeliveryRemoteDataSource,
) : DeliveryRepository {
    override suspend fun patchDelivery(
        orderId: Long,
        deliveryMethod: String,
        trackingNumber: String,
    ): Result<DeliveryDetail> = executeWithUiMock(
        mock = { UiMockData.deliveryDetail(orderId, trackingNumber) },
        real = {
            httpResponseHandler.safeApiCall {
                deliveryRemoteDataSource.patchDelivery(
                    orderId,
                    DeliveryRequestDto(
                        carrier = deliveryMethod,
                        trackingNumber = trackingNumber,
                    ),
                )
                    .handleApiResponse()
                    .getOrThrow()
                    .toDomain()
            }
        },
    )
}
