package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.artist.toDomain
import com.poti.android.data.mapper.artist.toDto
import com.poti.android.data.mapper.delivery.toDomain
import com.poti.android.data.mapper.delivery.toDto
import com.poti.android.data.mapper.participation.toDomain
import com.poti.android.data.mapper.party.toDomain
import com.poti.android.data.mapper.party.toRequestDto
import com.poti.android.data.remote.datasource.PartyRemoteDataSource
import com.poti.android.data.remote.dto.request.party.CreatePartyRequestDto
import com.poti.android.domain.model.artist.ArtistSearchResult
import com.poti.android.domain.model.artist.MemberPriceOption
import com.poti.android.domain.model.delivery.DeliveryOption
import com.poti.android.domain.model.history.MyPartyList
import com.poti.android.domain.model.party.PartyDetail
import com.poti.android.domain.model.party.PartyJoinInfo
import com.poti.android.domain.model.party.PartyJoinOption
import com.poti.android.domain.model.party.ProductPartyList
import com.poti.android.domain.repository.PartyRepository
import javax.inject.Inject

class PartyRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val partyRemoteDataSource: PartyRemoteDataSource,
) : PartyRepository {
    override suspend fun searchProductTitle(
        artistId: Long,
        keyword: String,
    ): Result<List<String>> = httpResponseHandler.safeApiCall {
        partyRemoteDataSource.searchProductTitle(artistId, keyword)
            .handleApiResponse()
            .getOrThrow()
            .titles
    }

    override suspend fun searchArtist(keyword: String): Result<List<ArtistSearchResult>> = httpResponseHandler.safeApiCall {
        partyRemoteDataSource.searchArtist(keyword)
            .handleApiResponse()
            .getOrThrow()
            .toDomain()
    }

    override suspend fun createPost(
        artistId: Long,
        product: String,
        description: String,
        deadline: String,
        bank: String,
        accountNumber: String,
        imageUrls: List<String>,
        options: List<MemberPriceOption>,
        shippings: List<DeliveryOption>,
    ): Result<Long> = httpResponseHandler.safeApiCall {
        partyRemoteDataSource.createParty(
            body = CreatePartyRequestDto(
                artistId = artistId,
                title = product,
                content = description,
                deadline = deadline,
                bankName = bank,
                accountNumber = accountNumber,
                imageUrls = imageUrls,
                options = options.map { it.toDto() },
                shippings = shippings.map { it.toDto() },
            ),
        )
            .handleApiResponse()
            .getOrThrow()
            .postId
    }

    override suspend fun getShippingOptions(): Result<List<DeliveryOption>> = httpResponseHandler.safeApiCall {
        partyRemoteDataSource
            .getShippingOptions()
            .handleApiResponse()
            .getOrThrow()
            .map { it.toDomain() }
    }

    override suspend fun getPartyDetail(partyId: Long): Result<PartyDetail> = httpResponseHandler.safeApiCall {
        partyRemoteDataSource.getPartyDetail(partyId = partyId)
            .handleApiResponse()
            .getOrThrow()
            .toDomain()
    }

    override suspend fun getPartyJoinOptions(partyId: Long): Result<PartyJoinOption> = httpResponseHandler.safeApiCall {
        partyRemoteDataSource.getPartyJoinOptions(partyId = partyId)
            .handleApiResponse()
            .getOrThrow()
            .toDomain()
    }

    override suspend fun postPartyJoin(joinInfo: PartyJoinInfo): Result<Long> = httpResponseHandler.safeApiCall {
        partyRemoteDataSource.postPartyJoin(joinInfo.toRequestDto())
            .handleApiResponse()
            .getOrThrow()
            .participationId
    }

    override suspend fun getMyRecruitList(status: String): Result<MyPartyList> = httpResponseHandler.safeApiCall {
        partyRemoteDataSource.getMyRecruitList(status)
            .handleApiResponse()
            .getOrThrow()
            .toDomain()
    }

    override suspend fun getProductPartyList(
        page: Int?,
        size: Int?,
        title: String,
        artistId: Long,
        sort: String,
        memberIds: List<Long>?,
    ): Result<ProductPartyList> = httpResponseHandler.safeApiCall {
        partyRemoteDataSource.getProductPartyList(
            page = page,
            size = size,
            title = title,
            artistId = artistId,
            sort = sort,
            memberIds = memberIds,
        )
            .handleApiResponse()
            .getOrThrow()
            .toDomain()
    }
}
