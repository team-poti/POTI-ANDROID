package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.history.toDomain
import com.poti.android.data.remote.datasource.PaymentRemoteDataSource
import com.poti.android.domain.model.history.ParticipantPaymentConfirm
import com.poti.android.domain.repository.PaymentRepository
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val paymentRemoteDataSource: PaymentRemoteDataSource
): PaymentRepository {
    override suspend fun patchPaymentConfirm(orderId: Long): Result<ParticipantPaymentConfirm> =
        httpResponseHandler.safeApiCall {
            paymentRemoteDataSource.patchPaymentConfirm(orderId)
                .handleApiResponse()
                .getOrThrow()
                .toDomain()
        }
}
