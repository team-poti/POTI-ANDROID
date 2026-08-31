package com.poti.android.data.repository

import com.poti.android.core.common.util.suspendRunCatching
import com.poti.android.core.fcm.FcmTokenProvider
import com.poti.android.core.fcm.remote.datasource.FcmRemoteDataSource
import com.poti.android.core.fcm.remote.dto.request.FcmTokenRequestDto
import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.model.handleNullableApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.local.datasource.AuthTokenStore
import com.poti.android.data.local.datasource.PreferenceDataSource
import com.poti.android.data.local.datasource.WithdrawalLocalDataCleaner
import com.poti.android.data.mapper.auth.toDomain
import com.poti.android.data.mock.UiMockData
import com.poti.android.data.mock.executeWithUiMock
import com.poti.android.data.remote.datasource.AuthRemoteDataSource
import com.poti.android.data.remote.dto.request.auth.LoginRequestDto
import com.poti.android.data.remote.dto.request.auth.WithdrawalRequestDto
import com.poti.android.domain.manager.AuthSessionManager
import com.poti.android.domain.model.auth.AuthState
import com.poti.android.domain.model.auth.SocialType
import com.poti.android.domain.model.auth.UserAuth
import com.poti.android.domain.model.auth.WithdrawalReason
import com.poti.android.domain.repository.AuthRepository
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val preferenceDataSource: PreferenceDataSource,
    private val authTokenStore: AuthTokenStore,
    private val authSessionManager: AuthSessionManager,
    private val fcmTokenProvider: FcmTokenProvider,
    private val fcmRemoteDataSource: FcmRemoteDataSource,
    private val withdrawalLocalDataCleaner: WithdrawalLocalDataCleaner,
) : AuthRepository {
    override fun observeAuthState(): Flow<AuthState> = authTokenStore.authState

    override suspend fun login(
        socialType: SocialType,
        token: String,
    ): Result<UserAuth> {
        withdrawalLocalDataCleaner.awaitCacheCleanup()

        return executeWithUiMock(
            mock = {
                UiMockData.userAuth.also {
                    authTokenStore.saveTokens(it.accessToken, it.refreshToken)
                    preferenceDataSource.saveSocialType(socialType)
                    preferenceDataSource.saveOnboardingState(!it.isNewUser)
                }
            },
            real = {
                httpResponseHandler.safeApiCall {
                    val requestDto = LoginRequestDto(
                        socialType = socialType.name,
                        token = token,
                    )
                    authRemoteDataSource.login(loginRequest = requestDto)
                        .handleApiResponse()
                        .getOrThrow()
                        .apply {
                            authTokenStore.saveTokens(accessToken, refreshToken)
                            preferenceDataSource.saveSocialType(socialType)
                            preferenceDataSource.saveOnboardingState(!isNewUser)
                        }
                        .toDomain()
                }.onSuccess { syncFcmToken() }
            },
        )
    }

    override suspend fun saveOnboardingState(isCompleted: Boolean): Result<Unit> = executeWithUiMock(
        mock = {
            preferenceDataSource.saveOnboardingState(isCompleted)
            Unit
        },
        real = {
            httpResponseHandler.safeApiCall {
                preferenceDataSource.saveOnboardingState(isCompleted)
                Unit
            }
        },
    )

    override suspend fun logout(): Result<Unit> = suspendRunCatching {
        deleteFcmToken()
        authTokenStore.clearAll()
        authSessionManager.triggerLogout()
    }

    override suspend fun getWithdrawalReasons(): Result<List<WithdrawalReason>> = executeWithUiMock(
        mock = { UiMockData.withdrawalReasons },
        real = {
            httpResponseHandler.safeApiCall {
                authRemoteDataSource.getWithdrawalReasons()
                    .handleApiResponse()
                    .getOrThrow()
                    .map { it.toDomain() }
            }
        },
    )

    override suspend fun withdrawal(reason: String): Result<Unit> = executeWithUiMock(
        mock = {
            authTokenStore.clearAll()
            authSessionManager.triggerLogout()
        },
        real = { withdrawalFromRemote(reason = reason) },
    )

    private suspend fun withdrawalFromRemote(reason: String): Result<Unit> {
        val socialType = httpResponseHandler.safeApiCall {
            preferenceDataSource.getSocialType()
        }.getOrElse { error ->
            return Result.failure(error)
        }

        val remoteResult = httpResponseHandler.safeApiCall {
            authRemoteDataSource.withdrawal(request = WithdrawalRequestDto(reason = reason))
                .handleNullableApiResponse()
                .getOrThrow()
            Unit
        }
        if (remoteResult.isFailure) return remoteResult

        finalizeWithdrawal(socialType = socialType)
        return Result.success(Unit)
    }

    private suspend fun finalizeWithdrawal(socialType: SocialType?) = withContext(NonCancellable) {
        try {
            withdrawalLocalDataCleaner.unlinkKakaoAccount(socialType = socialType)
            authTokenStore.clearAll()
            withdrawalLocalDataCleaner.clearCachesInBackground()
        } finally {
            authSessionManager.triggerLogout()
        }
    }

    private suspend fun syncFcmToken() {
        val fcmToken = fcmTokenProvider.getToken() ?: return
        suspendRunCatching {
            fcmRemoteDataSource.postFcmToken(request = FcmTokenRequestDto(fcmToken))
        }.onFailure { Timber.e(it, "Failed to save FCM token") }
    }

    private suspend fun deleteFcmToken() {
        val fcmToken = fcmTokenProvider.getToken() ?: return
        suspendRunCatching {
            fcmRemoteDataSource.deleteFcmToken(fcmToken)
        }.onFailure { Timber.e(it, "Failed to delete FCM token") }
    }
}
