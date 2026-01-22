package com.poti.android.data.mapper.review

import com.poti.android.data.remote.dto.response.review.ReviewResponseDto
import com.poti.android.domain.model.review.ReviewResult

fun ReviewResponseDto.toDomain(): ReviewResult = ReviewResult(
    reviewId = this.reviewId
)
