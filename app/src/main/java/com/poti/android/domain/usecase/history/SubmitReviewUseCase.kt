package com.poti.android.domain.usecase.history

import com.poti.android.domain.repository.ReviewRepository
import javax.inject.Inject

class SubmitReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(
        transactionId: Long,
        star: Int,
    ): Result<Long> = reviewRepository.postReview(transactionId, star)
}
