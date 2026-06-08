package com.poti.android.domain.usecase.history

import com.poti.android.domain.model.history.MyPartyList
import com.poti.android.domain.repository.PartyRepository
import javax.inject.Inject

class GetMyRecruitListUseCase @Inject constructor(
    private val partyRepository: PartyRepository,
) {
    suspend operator fun invoke(status: String): Result<MyPartyList> =
        partyRepository.getMyRecruitList(status)
}
