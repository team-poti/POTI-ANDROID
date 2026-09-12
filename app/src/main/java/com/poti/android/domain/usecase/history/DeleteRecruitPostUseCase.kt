package com.poti.android.domain.usecase.history

import com.poti.android.domain.repository.PartyRepository
import javax.inject.Inject

class DeleteRecruitPostUseCase @Inject constructor(
    private val partyRepository: PartyRepository,
) {
    suspend operator fun invoke(postId: Long): Result<Unit> =
        partyRepository.deleteRecruitPost(postId)
}
