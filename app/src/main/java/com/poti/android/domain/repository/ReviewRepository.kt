package com.poti.android.domain.repository

import com.poti.android.domain.model.review.ReviewResult

interface ReviewRepository {
    suspend fun postReview(transactionId: Long, star: Int): Result<ReviewResult>
}
