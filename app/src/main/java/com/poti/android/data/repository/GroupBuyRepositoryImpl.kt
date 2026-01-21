package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.history.toDomain
import com.poti.android.data.remote.datasource.GroupBuyRemoteDataSource
import com.poti.android.domain.model.history.RecruiterDetail
import com.poti.android.domain.repository.GroupBuyRepository
import javax.inject.Inject

class GroupBuyRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val groupBuyRemoteDataSource: GroupBuyRemoteDataSource,
) : GroupBuyRepository {
    override suspend fun getPostSale(postId: Long): Result<RecruiterDetail> =
        httpResponseHandler.safeApiCall {
            groupBuyRemoteDataSource.getPostSale(postId)
                .handleApiResponse()
                .getOrThrow()
                .toDomain()
        }
}
