package com.poti.android.domain.usecase.history

import com.poti.android.domain.repository.ParticipationRepository
import javax.inject.Inject

class ConfirmDeliveryUseCase @Inject constructor(
    private val participationRepository: ParticipationRepository,
) {
    suspend operator fun invoke(participationId: Long): Result<Long> =
        participationRepository.patchDeliveryConfirm(participationId)
}
