package com.poti.android.domain.usecase.user

import com.poti.android.domain.model.delivery.DeliveryInfo
import com.poti.android.domain.repository.UserRepository
import javax.inject.Inject

class GetMyAddressUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Result<DeliveryInfo?> = userRepository.getMyAddress()
}
