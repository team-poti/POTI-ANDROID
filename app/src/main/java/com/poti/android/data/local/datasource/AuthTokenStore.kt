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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
    val generation: Long = 0L,
)

@Singleton
class AuthTokenStore @Inject constructor(
    private val preferenceDataSource: PreferenceDataSource,
    @ApplicationScope private val externalScope: CoroutineScope,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    private val cacheLock = Any()
    private val persistenceMutex = Mutex()
    private val _tokenState = MutableStateFlow(AuthTokenState())
    val tokenState: StateFlow<AuthTokenState> = _tokenState.asStateFlow()

    val authState: Flow<AuthState> = combine(
        preferenceDataSource.authState,
        tokenState,
    ) { persistedAuthState, tokenState ->
        AuthState(
            accessToken = tokenState.tokenPair?.accessToken,
            isOnboardingFinished = persistedAuthState.isOnboardingFinished,
            isInitialized = tokenState.isInitialized,
        )
    }

    val cachedTokenPair: TokenPair?
        get() = tokenState.value.tokenPair

    val cachedAccessToken: String?
        get() = cachedTokenPair?.accessToken

    init {
        externalScope.launch(ioDispatcher) {
            preferenceDataSource.tokenPair.collect { tokenPair ->
                initializeFromStorage(tokenPair)
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
        val generation = updateCachedTokens(tokenPair)
        persistTokensIfCurrent(tokenPair, generation)
    }

    suspend fun clearTokens() {
        val generation = clearCachedTokens()
        persistTokenClearIfCurrent(generation)
    }

    suspend fun clearAll() {
        val generation = clearCachedTokens()
        persistenceMutex.withLock {
            if (isCurrentClearedGeneration(generation)) {
                preferenceDataSource.clearAll()
            }
        }
    }

    fun updateCachedTokens(tokenPair: TokenPair): Long = synchronized(cacheLock) {
        val generation = tokenState.value.generation + 1
        _tokenState.value = AuthTokenState(
            isInitialized = true,
            tokenPair = tokenPair,
            generation = generation,
        )
        generation
    }

    fun clearCachedTokens(): Long = synchronized(cacheLock) {
        val generation = tokenState.value.generation + 1
        _tokenState.value = AuthTokenState(
            isInitialized = true,
            generation = generation,
        )
        generation
    }

    suspend fun persistTokensIfCurrent(
        tokenPair: TokenPair,
        generation: Long,
    ): Boolean = persistenceMutex.withLock {
        if (!isCurrentGeneration(tokenPair, generation)) return@withLock false

        preferenceDataSource.saveTokens(tokenPair.accessToken, tokenPair.refreshToken)
        true
    }

    suspend fun persistTokenClearIfCurrent(generation: Long): Boolean = persistenceMutex.withLock {
        if (!isCurrentClearedGeneration(generation)) return@withLock false

        preferenceDataSource.clearTokens()
        true
    }

    private fun initializeFromStorage(tokenPair: TokenPair?) {
        synchronized(cacheLock) {
            if (tokenState.value.isInitialized) return

            _tokenState.value = AuthTokenState(
                isInitialized = true,
                tokenPair = tokenPair,
            )
        }
    }

    private fun isCurrentGeneration(
        tokenPair: TokenPair,
        generation: Long,
    ): Boolean = synchronized(cacheLock) {
        tokenState.value.generation == generation && tokenState.value.tokenPair == tokenPair
    }

    private fun isCurrentClearedGeneration(generation: Long): Boolean = synchronized(cacheLock) {
        tokenState.value.generation == generation && tokenState.value.tokenPair == null
    }

    private companion object {
        const val INITIALIZATION_TIMEOUT_MILLIS = 1_000L
    }
}
