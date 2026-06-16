package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.home.toDomain
import com.poti.android.data.mock.UiMockData
import com.poti.android.data.mock.executeWithUiMock
import com.poti.android.data.remote.datasource.HomeRemoteDataSource
import com.poti.android.domain.model.home.HomeContent
import com.poti.android.domain.model.party.ProductCategory
import com.poti.android.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val homeRemoteDataSource: HomeRemoteDataSource,
) : HomeRepository {
    override suspend fun getHomeContent(): Result<HomeContent> = executeWithUiMock(
        mock = { UiMockData.homeContent },
        real = {
            httpResponseHandler.safeApiCall {
                homeRemoteDataSource.getHomeContent()
                    .handleApiResponse()
                    .getOrThrow()
                    .toDomain()
            }
        },
    )

    override suspend fun getGoodsCategoryList(
        page: Int?,
        size: Int?,
        sort: String?,
        artistId: Long?,
    ): Result<ProductCategory> = executeWithUiMock(
        mock = { UiMockData.productCategory },
        real = {
            httpResponseHandler.safeApiCall {
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
        },
    )
}
