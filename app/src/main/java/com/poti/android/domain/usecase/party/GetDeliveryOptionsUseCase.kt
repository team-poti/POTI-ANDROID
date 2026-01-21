package com.poti.android.domain.usecase.party

import com.poti.android.domain.model.delivery.DeliveryOption
import com.poti.android.domain.repository.PartyRepository
import javax.inject.Inject

class GetDeliveryOptionsUseCase @Inject constructor(
    private val partyRepository: PartyRepository,
) {
    suspend operator fun invoke(): Result<List<DeliveryOption>> = partyRepository.getShippingOptions()
}
