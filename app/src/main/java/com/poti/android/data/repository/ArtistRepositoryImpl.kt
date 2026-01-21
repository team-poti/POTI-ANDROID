package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.artist.toDomain
import com.poti.android.data.mapper.artist.toPriceDomain
import com.poti.android.data.remote.datasource.ArtistRemoteDataSource
import com.poti.android.domain.model.artist.Artist
import com.poti.android.domain.model.artist.Member
import com.poti.android.domain.model.artist.MemberPriceOption
import com.poti.android.domain.repository.ArtistRepository
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val artistRemoteDataSource: ArtistRemoteDataSource,
) : ArtistRepository {
    override suspend fun getArtists(): Result<List<Artist>> = httpResponseHandler.safeApiCall {
        artistRemoteDataSource.getArtists()
            .handleApiResponse()
            .getOrThrow()
            .toDomain()
    }

    override suspend fun getMemberList(artistId: Long): Result<List<Member>> = httpResponseHandler.safeApiCall {
        artistRemoteDataSource.getMemberList(artistId)
            .handleApiResponse()
            .getOrThrow()
            .toDomain()
    }

    override suspend fun getMemberListWithPrice(artistId: Long): Result<List<MemberPriceOption>> = httpResponseHandler.safeApiCall {
        artistRemoteDataSource.getMemberList(artistId)
            .handleApiResponse()
            .getOrThrow()
            .toPriceDomain()
    }
}
