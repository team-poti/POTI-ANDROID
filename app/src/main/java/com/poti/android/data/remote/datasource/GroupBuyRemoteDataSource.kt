package com.poti.android.data.remote.datasource

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.history.GroupBuyPostSaleDto
import com.poti.android.data.remote.service.GroupBuyService
import javax.inject.Inject

class GroupBuyRemoteDataSource @Inject constructor(
    private val groupBuyService: GroupBuyService,
) {
    suspend fun getPostSale(postId: Long): BaseResponse<GroupBuyPostSaleDto> =
        groupBuyService.getPostSale(postId)
}
