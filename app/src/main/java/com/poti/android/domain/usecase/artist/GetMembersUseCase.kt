package com.poti.android.domain.usecase.artist

import com.poti.android.domain.model.artist.Member
import com.poti.android.domain.repository.ArtistRepository
import javax.inject.Inject

class GetMembersUseCase @Inject constructor(
    private val artistRepository: ArtistRepository,
) {
    suspend operator fun invoke(
        artistId: Long,
    ): Result<List<Member>> = artistRepository.getMemberList(artistId)
}
