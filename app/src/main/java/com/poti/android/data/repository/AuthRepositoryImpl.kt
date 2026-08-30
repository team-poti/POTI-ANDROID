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
import com.poti.android.data.mapper.auth.toDomain
import com.poti.android.data.mock.UiMockData
import com.poti.android.data.mock.executeWithUiMock
import com.poti.android.data.remote.datasource.AuthRemoteDataSource
import com.poti.android.data.remote.dto.request.auth.LoginRequestDto
import com.poti.android.domain.manager.AuthSessionManager
import com.poti.android.domain.model.auth.AuthState
import com.poti.android.domain.model.auth.SocialType
import com.poti.android.domain.model.auth.UserAuth
import com.poti.android.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
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
) : AuthRepository {
    override fun observeAuthState(): Flow<AuthState> = authTokenStore.authState

    override suspend fun login(
        socialType: SocialType,
        token: String,
    ): Result<UserAuth> = executeWithUiMock(
        mock = {
            UiMockData.userAuth.also {
                authTokenStore.saveTokens(it.accessToken, it.refreshToken)
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
                        preferenceDataSource.saveOnboardingState(!isNewUser)
                    }
                    .toDomain()
            }.onSuccess { syncFcmToken() }
        },
    )

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

    override suspend fun withdrawal(): Result<Unit> = executeWithUiMock(
        mock = {
            authTokenStore.clearAll()
            authSessionManager.triggerLogout()
        },
        real = {
            deleteFcmToken()
            httpResponseHandler.safeApiCall {
                authRemoteDataSource.withdrawal()
                    .handleNullableApiResponse()
                    .getOrThrow()
                authTokenStore.clearAll()
                authSessionManager.triggerLogout()
            }
        },
    )

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
