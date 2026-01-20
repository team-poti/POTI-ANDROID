package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.post.toDomain
import com.poti.android.data.remote.datasource.PostRemoteDataSource
import com.poti.android.domain.model.party.PartyDetail
import com.poti.android.domain.repository.PartyRepository
import javax.inject.Inject

class PartyRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val postRemoteDataSource: PostRemoteDataSource,
) : PartyRepository {
    override suspend fun getPartyDetail(partyId: Long): Result<PartyDetail> = httpResponseHandler.safeApiCall {
        postRemoteDataSource.getPostDetail(postId = partyId)
            .handleApiResponse()
            .getOrThrow()
            .toDomain()
    }
}
