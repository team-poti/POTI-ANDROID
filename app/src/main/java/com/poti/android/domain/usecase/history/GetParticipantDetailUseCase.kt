package com.poti.android.domain.usecase.history

import com.poti.android.domain.model.history.ParticipantDetail
import com.poti.android.domain.repository.ParticipationRepository
import javax.inject.Inject

class GetParticipantDetailUseCase @Inject constructor(
    private val participationRepository: ParticipationRepository,
) {
    suspend operator fun invoke(participationId: Long): Result<ParticipantDetail> =
        participationRepository.getParticipantDetail(participationId)
}
