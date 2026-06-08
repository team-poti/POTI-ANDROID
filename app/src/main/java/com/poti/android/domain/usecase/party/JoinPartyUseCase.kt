package com.poti.android.domain.usecase.party

import com.poti.android.domain.model.party.PartyJoinInfo
import com.poti.android.domain.repository.PartyRepository
import javax.inject.Inject

class JoinPartyUseCase @Inject constructor(
    private val partyRepository: PartyRepository,
) {
    suspend operator fun invoke(joinInfo: PartyJoinInfo): Result<Long> =
        partyRepository.postPartyJoin(joinInfo)
}
