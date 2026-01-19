package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.artist.toDomain
import com.poti.android.data.mapper.artist.toDto
import com.poti.android.data.mapper.delivery.toDto
import com.poti.android.data.remote.datasource.PostRemoteDataSource
import com.poti.android.data.remote.dto.request.post.CreatePostRequestDto
import com.poti.android.domain.model.artist.ArtistSearchResult
import com.poti.android.domain.model.artist.MemberPriceOption
import com.poti.android.domain.model.delivery.DeliveryOption
import com.poti.android.domain.repository.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val postRemoteDataSource: PostRemoteDataSource,
) : PostRepository {
    override suspend fun searchProductTitle(artistId: Long, keyword: String): Result<List<String>> = httpResponseHandler.safeApiCall {
        postRemoteDataSource.searchProductTitle(artistId, keyword)
            .handleApiResponse()
            .getOrThrow()
            .titles
    }

    override suspend fun searchArtist(keyword: String): Result<List<ArtistSearchResult>> = httpResponseHandler.safeApiCall {
        postRemoteDataSource.searchArtist(keyword)
            .handleApiResponse()
            .getOrThrow()
            .toDomain()
    }

    override suspend fun createPost(
        artistId: Long,
        product: String,
        descripton: String,
        deadline: String,
        bank: String,
        accountNumber: String,
        imageUrls: List<String>,
        options: List<MemberPriceOption>,
        shippings: List<DeliveryOption>,
    ): Result<Long> = httpResponseHandler.safeApiCall {
        postRemoteDataSource.createPost(
            body = CreatePostRequestDto(
                artistId = artistId,
                title = product,
                content = descripton,
                deadline = deadline,
                bankName = bank,
                accountNumber = accountNumber,
                imageUrls = imageUrls,
                options = options.map { it.toDto() },
                shippings = shippings.map { it.toDto() }
            )
        )
            .handleApiResponse()
            .getOrThrow()
            .postId
    }
}
