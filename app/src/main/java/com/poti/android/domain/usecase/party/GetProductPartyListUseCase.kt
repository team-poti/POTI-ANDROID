package com.poti.android.domain.usecase.party

import com.poti.android.domain.model.party.ProductPartyList
import com.poti.android.domain.repository.PartyRepository
import javax.inject.Inject

class GetProductPartyListUseCase @Inject constructor(
    private val partyRepository: PartyRepository,
) {
    suspend operator fun invoke(
        page: Int?,
        size: Int?,
        title: String,
        artistId: Long,
        sort: String,
        memberIds: List<Long>?,
    ): Result<ProductPartyList> = partyRepository.getProductPartyList(
        page,
        size,
        title,
        artistId,
        sort,
        memberIds,
    )
}
