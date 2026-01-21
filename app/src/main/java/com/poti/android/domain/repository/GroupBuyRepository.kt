package com.poti.android.domain.repository

import com.poti.android.domain.model.history.RecruiterDetail

interface GroupBuyRepository {
    suspend fun getPostSale(postId: Long): Result<RecruiterDetail>
}
