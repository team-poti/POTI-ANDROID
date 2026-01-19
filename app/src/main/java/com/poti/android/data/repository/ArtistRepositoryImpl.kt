package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.artist.toDomain
import com.poti.android.data.remote.datasource.ArtistRemoteDataSource
import com.poti.android.domain.model.artist.Artist
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
}
