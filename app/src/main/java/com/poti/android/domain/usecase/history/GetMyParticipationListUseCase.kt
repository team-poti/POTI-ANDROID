package com.poti.android.domain.usecase.history

import com.poti.android.domain.model.history.MyPartyList
import com.poti.android.domain.repository.ParticipationRepository
import javax.inject.Inject

class GetMyParticipationListUseCase @Inject constructor(
    private val participationRepository: ParticipationRepository,
) {
    suspend operator fun invoke(status: String): Result<MyPartyList> =
        participationRepository.getMyParticipationList(status)
}
