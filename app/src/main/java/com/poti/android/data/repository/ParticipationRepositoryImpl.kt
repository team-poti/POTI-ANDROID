package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.history.toDomain
import com.poti.android.data.mock.UiMockData
import com.poti.android.data.mock.executeWithUiMock
import com.poti.android.data.remote.datasource.ParticipantRemoteDataSource
import com.poti.android.domain.model.history.MyPartyList
import com.poti.android.domain.model.history.ParticipantDetail
import com.poti.android.domain.repository.ParticipationRepository
import jakarta.inject.Inject

class ParticipationRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val participationRemoteDataSource: ParticipantRemoteDataSource,
) : ParticipationRepository {
    override suspend fun getMyParticipationList(status: String): Result<MyPartyList> =
        executeWithUiMock(
            mock = { UiMockData.myPartyList(status, participation = true) },
            real = {
                httpResponseHandler.safeApiCall {
                    participationRemoteDataSource.getMyPartyList(status)
                        .handleApiResponse()
                        .getOrThrow()
                        .toDomain()
                }
            },
        )

    override suspend fun getParticipantDetail(participationId: Long): Result<ParticipantDetail> =
        executeWithUiMock(
            mock = { UiMockData.participantDetail.copy(participationId = participationId) },
            real = {
                httpResponseHandler.safeApiCall {
                    participationRemoteDataSource.getParticipantDetail(participationId)
                        .handleApiResponse()
                        .getOrThrow()
                        .toDomain()
                }
            },
        )

    override suspend fun patchDeliveryConfirm(participationId: Long): Result<Long> =
        executeWithUiMock(
            mock = { UiMockData.userProfile.userId },
            real = {
                httpResponseHandler.safeApiCall {
                    participationRemoteDataSource.patchDeliveryConfirm(participationId)
                        .handleApiResponse()
                        .getOrThrow()
                        .leaderUserId
                }
            },
        )
}
