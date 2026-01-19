package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.home.toDomain
import com.poti.android.data.remote.datasource.HomeRemoteDataSource
import com.poti.android.domain.model.home.HomeContent
import com.poti.android.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val homeRemoteDataSource: HomeRemoteDataSource,
) : HomeRepository {
    override suspend fun getHomeContent(): Result<HomeContent> = httpResponseHandler.safeApiCall {
        homeRemoteDataSource.getHomeContent()
            .handleApiResponse()
            .getOrThrow()
            .toDomain()
    }
}
