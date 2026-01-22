package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.review.ReviewRequestDto
import com.poti.android.data.remote.dto.response.review.ReviewResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ReviewService {
    @POST("/api/v1/reviews")
    suspend fun postReview(@Body reviewReq: ReviewRequestDto): BaseResponse<ReviewResponseDto>
}
