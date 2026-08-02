package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
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
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val preferenceDataSource: PreferenceDataSource,
    private val authTokenStore: AuthTokenStore,
    private val authSessionManager: AuthSessionManager,
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
            }
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

    override suspend fun withdrawal(): Result<Unit> = executeWithUiMock(
        mock = {
            authTokenStore.clearAll()
            authSessionManager.triggerLogout()
        },
        real = {
            httpResponseHandler.safeApiCall {
                authRemoteDataSource.withdrawal()
                authTokenStore.clearAll()
                authSessionManager.triggerLogout()
            }
        },
    )
}
