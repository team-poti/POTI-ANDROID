package com.poti.android.domain.usecase.artist

import com.poti.android.domain.model.artist.MemberPriceOption
import com.poti.android.domain.repository.ArtistRepository
import javax.inject.Inject

class GetMembersWithPriceUseCase @Inject constructor(
    private val artistRepository: ArtistRepository,
) {
    suspend operator fun invoke(
        artistId: Long,
    ): Result<List<MemberPriceOption>> = artistRepository.getMemberListWithPrice(artistId)
}
