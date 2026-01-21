package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.history.GroupBuyPostSaleDto
import retrofit2.http.GET
import retrofit2.http.Path

interface GroupBuyService {
    @GET("/api/v1/posts/sale/{postId}")
    suspend fun getPostSale(
        @Path("postId") postId: Long,
    ): BaseResponse<GroupBuyPostSaleDto>
}
