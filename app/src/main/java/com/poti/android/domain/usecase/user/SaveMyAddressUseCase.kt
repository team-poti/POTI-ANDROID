package com.poti.android.domain.usecase.user

import com.poti.android.domain.model.delivery.DeliveryInfo
import com.poti.android.domain.repository.UserRepository
import javax.inject.Inject

class SaveMyAddressUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        deliveryInfo: DeliveryInfo,
    ): Result<Unit> = userRepository.saveMyAddress(deliveryInfo = deliveryInfo)
}
