package com.poti.android.domain.usecase.party

import com.poti.android.domain.model.party.PartyDetail
import com.poti.android.domain.repository.PartyRepository
import javax.inject.Inject

class GetPartyDetailUseCase @Inject constructor(
    private val partyRepository: PartyRepository,
) {
    suspend operator fun invoke(partyId: Long): Result<PartyDetail> =
        partyRepository.getPartyDetail(partyId)
}
