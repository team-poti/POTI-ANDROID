package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.remote.datasource.ReviewRemoteDataSource
import com.poti.android.domain.repository.ReviewRepository
import timber.log.Timber
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val reviewRemoteDataSource: ReviewRemoteDataSource,
) : ReviewRepository {
    override suspend fun postReview(
        transactionId: Long,
        star: Int,
    ): Result<Long> =
        httpResponseHandler.safeApiCall {
            try {
                reviewRemoteDataSource.postReview(transactionId = transactionId, star = star)
                    .handleApiResponse()
                    .getOrThrow()
                    .reviewId
            } catch (e: Exception) {
                Timber.e(e, "postReview error")
                throw e
            }
        }
}
