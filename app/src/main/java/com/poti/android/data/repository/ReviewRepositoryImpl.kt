package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.review.toDomain
import com.poti.android.data.remote.datasource.ReviewRemoteDataSource
import com.poti.android.domain.model.review.ReviewResult
import com.poti.android.domain.repository.ReviewRepository
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val reviewRemoteDataSource: ReviewRemoteDataSource
): ReviewRepository {
    override suspend fun postReview(transactionId: Long, star: Int): Result<ReviewResult> =
        httpResponseHandler.safeApiCall {
            reviewRemoteDataSource.postReview(
                transactionId = transactionId,
                star = star
            ).handleApiResponse()
                .getOrThrow()
                .toDomain()
        }
}
