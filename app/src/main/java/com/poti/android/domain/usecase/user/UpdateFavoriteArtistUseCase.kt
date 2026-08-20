package com.poti.android.domain.usecase.user

import com.poti.android.domain.repository.UserRepository
import javax.inject.Inject

class UpdateFavoriteArtistUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        artistId: Long,
    ): Result<Unit> = userRepository.patchFavoriteArtist(artistId)
}
