package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.home.toDomain
import com.poti.android.data.remote.datasource.HomeRemoteDataSource
import com.poti.android.domain.model.home.HomeContent
import com.poti.android.domain.model.party.ProductCategory
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

    override suspend fun getGoodsCategoryList(
        page: Int?,
        size: Int?,
        sort: String?,
        artistId: Long?,
    ): Result<ProductCategory> = httpResponseHandler.safeApiCall {
        homeRemoteDataSource.getGoodsCategoryList(
            page = page,
            size = size,
            sort = sort,
            artistId = artistId,
        )
            .handleApiResponse()
            .getOrThrow()
            .toDomain()
    }
}
