package com.poti.android.domain.usecase.party

import com.poti.android.domain.model.party.PartyJoinOption
import com.poti.android.domain.repository.PartyRepository
import javax.inject.Inject

class GetPartyJoinOptionsUseCase @Inject constructor(
    private val partyRepository: PartyRepository,
) {
    suspend operator fun invoke(partyId: Long): Result<PartyJoinOption> =
        partyRepository.getPartyJoinOptions(partyId)
}
