package com.poti.android.domain.usecase.history

import com.poti.android.domain.model.history.DeliveryDetail
import com.poti.android.domain.repository.DeliveryRepository
import javax.inject.Inject

class RegisterDeliveryUseCase @Inject constructor(
    private val deliveryRepository: DeliveryRepository,
) {
    suspend operator fun invoke(
        orderId: Long,
        deliveryMethod: String,
        trackingNumber: String,
    ): Result<DeliveryDetail> = deliveryRepository.patchDelivery(
        orderId,
        deliveryMethod,
        trackingNumber,
    )
}
