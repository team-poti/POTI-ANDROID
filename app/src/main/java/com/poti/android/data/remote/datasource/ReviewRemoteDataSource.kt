package com.poti.android.data.remote.datasource

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.review.ReviewRequestDto
import com.poti.android.data.remote.dto.response.review.ReviewResponseDto
import com.poti.android.data.remote.service.ReviewService
import javax.inject.Inject

class ReviewRemoteDataSource @Inject constructor(
    private val reviewService: ReviewService
) {
    suspend fun postReview(transactionId: Long, star: Int): BaseResponse<ReviewResponseDto> =
        reviewService.postReview(
            reviewReq = ReviewRequestDto(
                transactionId = transactionId,
                star = star
            )
        )
}
