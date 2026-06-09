package com.poti.android.domain.usecase.history

import com.poti.android.domain.model.history.RecruiterDetail
import com.poti.android.domain.repository.PartyRepository
import javax.inject.Inject

class GetRecruitDetailUseCase @Inject constructor(
    private val partyRepository: PartyRepository,
) {
    suspend operator fun invoke(postId: Long): Result<RecruiterDetail> =
        partyRepository.getRecruitDetail(postId)
}
