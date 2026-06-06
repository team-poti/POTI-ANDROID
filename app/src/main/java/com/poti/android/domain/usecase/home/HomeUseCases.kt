package com.poti.android.domain.usecase.home

import com.poti.android.domain.model.home.HomeContent
import com.poti.android.domain.model.party.ProductCategory
import com.poti.android.domain.repository.HomeRepository
import javax.inject.Inject

class GetHomeContentUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
) {
    suspend operator fun invoke(): Result<HomeContent> = homeRepository.getHomeContent()
}

class GetGoodsCategoryListUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
) {
    suspend operator fun invoke(
        page: Int? = null,
        size: Int? = null,
        sort: String? = null,
        artistId: Long? = null,
    ): Result<ProductCategory> = homeRepository.getGoodsCategoryList(page, size, sort, artistId)
}
