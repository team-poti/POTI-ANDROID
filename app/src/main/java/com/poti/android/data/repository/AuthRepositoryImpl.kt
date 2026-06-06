package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.local.datasource.PreferenceDataSource
import com.poti.android.data.mapper.auth.toDomain
import com.poti.android.data.mock.UiMockData
import com.poti.android.data.mock.useUiMockWhenEnabled
import com.poti.android.data.remote.datasource.AuthRemoteDataSource
import com.poti.android.data.remote.dto.request.auth.LoginRequestDto
import com.poti.android.domain.manager.AuthSessionManager
import com.poti.android.domain.model.auth.AuthState
import com.poti.android.domain.model.auth.UserAuth
import com.poti.android.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val preferenceDataSource: PreferenceDataSource,
    private val authSessionManager: AuthSessionManager,
) : AuthRepository {
    override fun observeAuthState(): Flow<AuthState> = preferenceDataSource.authState

    override suspend fun login(
        socialType: String,
        token: String,
    ): Result<UserAuth> =
        httpResponseHandler.safeApiCall {
            val requestDto = LoginRequestDto(
                socialType = socialType,
                token = token,
            )
            authRemoteDataSource.login(loginRequest = requestDto)
                .handleApiResponse()
                .getOrThrow()
                .apply {
                    preferenceDataSource.saveTokens(accessToken, refreshToken)
                    preferenceDataSource.saveOnboardingState(!isNewUser)
                }
                .toDomain()
        }.useUiMockWhenEnabled {
            UiMockData.userAuth.also {
                preferenceDataSource.saveTokens(it.accessToken, it.refreshToken)
                preferenceDataSource.saveOnboardingState(!it.isNewUser)
            }
        }

    override suspend fun saveOnboardingState(isCompleted: Boolean): Result<Unit> = httpResponseHandler.safeApiCall {
        preferenceDataSource.saveOnboardingState(isCompleted)
        Unit
    }.useUiMockWhenEnabled {
        preferenceDataSource.saveOnboardingState(isCompleted)
        Unit
    }

    override suspend fun withdrawal(): Result<Unit> = httpResponseHandler.safeApiCall {
        authRemoteDataSource.withdrawal()
        preferenceDataSource.clearAll()
        authSessionManager.triggerLogout()
    }.useUiMockWhenEnabled {
        preferenceDataSource.clearAll()
        authSessionManager.triggerLogout()
    }
}
