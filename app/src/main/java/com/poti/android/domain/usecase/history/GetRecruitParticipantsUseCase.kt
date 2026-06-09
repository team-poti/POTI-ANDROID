package com.poti.android.domain.usecase.history

import com.poti.android.domain.model.history.ParticipantManageDetail
import com.poti.android.domain.repository.PartyRepository
import javax.inject.Inject

class GetRecruitParticipantsUseCase @Inject constructor(
    private val partyRepository: PartyRepository,
) {
    suspend operator fun invoke(postId: Long): Result<ParticipantManageDetail> =
        partyRepository.getRecruitPostParticipant(postId)
}
