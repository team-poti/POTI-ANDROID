package com.poti.android.domain.repository

interface ReviewRepository {
    suspend fun postReview(
        transactionId: Long,
        star: Int,
    ): Result<Long>
}
