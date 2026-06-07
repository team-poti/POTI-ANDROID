package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.history.toDomain
import com.poti.android.data.mapper.payment.toDomain
import com.poti.android.data.mock.UiMockData
import com.poti.android.data.mock.executeWithUiMock
import com.poti.android.data.remote.datasource.PaymentRemoteDataSource
import com.poti.android.domain.model.history.ParticipantPaymentConfirm
import com.poti.android.domain.model.payment.PaymentResult
import com.poti.android.domain.repository.PaymentRepository
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val paymentRemoteDataSource: PaymentRemoteDataSource,
) : PaymentRepository {
    override suspend fun patchPaymentConfirm(orderId: Long): Result<ParticipantPaymentConfirm> =
        executeWithUiMock(
            mock = { UiMockData.paymentConfirm(orderId) },
            real = {
                httpResponseHandler.safeApiCall {
                    paymentRemoteDataSource.patchPaymentConfirm(orderId)
                        .handleApiResponse()
                        .getOrThrow()
                        .toDomain()
                }
            },
        )

    override suspend fun postPayment(
        orderId: Long,
        depositorName: String,
        depositAt: String,
    ): Result<PaymentResult> = executeWithUiMock(
        mock = { UiMockData.paymentResult(orderId) },
        real = {
            httpResponseHandler.safeApiCall {
                paymentRemoteDataSource.postPayment(
                    orderId = orderId,
                    depositorName = depositorName,
                    depositAt = depositAt,
                ).handleApiResponse()
                    .getOrThrow()
                    .toDomain()
            }
        },
    )
}
