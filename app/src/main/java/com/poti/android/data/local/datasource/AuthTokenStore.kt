package com.poti.android.data.local.datasource

import com.poti.android.di.ApplicationScope
import com.poti.android.di.IoDispatcher
import com.poti.android.domain.model.auth.AuthState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton

data class TokenPair(
    val accessToken: String,
    val refreshToken: String,
)

data class AuthTokenState(
    val isInitialized: Boolean = false,
    val tokenPair: TokenPair? = null,
)

@Singleton
class AuthTokenStore @Inject constructor(
    private val preferenceDataSource: PreferenceDataSource,
    @ApplicationScope private val externalScope: CoroutineScope,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    private val _tokenState = MutableStateFlow(AuthTokenState())
    val tokenState: StateFlow<AuthTokenState> = _tokenState.asStateFlow()

    val authState: Flow<AuthState> = combine(
        preferenceDataSource.authState,
        tokenState,
    ) { authState, tokenState ->
        authState.copy(isInitialized = tokenState.isInitialized)
    }

    val cachedTokenPair: TokenPair?
        get() = tokenState.value.tokenPair

    val cachedAccessToken: String?
        get() = cachedTokenPair?.accessToken

    init {
        externalScope.launch(ioDispatcher) {
            preferenceDataSource.tokenPair.collect { tokenPair ->
                _tokenState.value = AuthTokenState(
                    isInitialized = true,
                    tokenPair = tokenPair,
                )
            }
        }
    }

    fun ensureInitializedBlocking(): Boolean {
        if (tokenState.value.isInitialized) return true

        return runBlocking {
            withTimeoutOrNull(INITIALIZATION_TIMEOUT_MILLIS) {
                awaitInitialized()
            } != null
        }
    }

    suspend fun awaitInitialized(): AuthTokenState = tokenState.first { it.isInitialized }

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
    ) {
        val tokenPair = TokenPair(accessToken, refreshToken)
        updateCachedTokens(tokenPair)
        persistTokens(tokenPair)
    }

    suspend fun clearTokens() {
        clearCachedTokens()
        persistTokenClear()
    }

    suspend fun clearAll() {
        _tokenState.value = AuthTokenState(isInitialized = true)
        preferenceDataSource.clearAll()
    }

    fun updateCachedTokens(tokenPair: TokenPair) {
        _tokenState.value = AuthTokenState(
            isInitialized = true,
            tokenPair = tokenPair,
        )
    }

    fun clearCachedTokens() {
        _tokenState.value = AuthTokenState(isInitialized = true)
    }

    suspend fun persistTokens(tokenPair: TokenPair) {
        preferenceDataSource.saveTokens(tokenPair.accessToken, tokenPair.refreshToken)
    }

    suspend fun persistTokenClear() {
        preferenceDataSource.clearTokens()
    }

    private companion object {
        const val INITIALIZATION_TIMEOUT_MILLIS = 1_000L
    }
}
