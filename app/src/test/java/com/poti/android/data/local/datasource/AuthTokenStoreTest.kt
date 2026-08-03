package com.poti.android.data.local.datasource

import com.poti.android.domain.model.auth.AuthState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class AuthTokenStoreTest {
    private val testDispatcher = StandardTestDispatcher()
    private val preferenceDataSource: PreferenceDataSource = mock(PreferenceDataSource::class.java)

    private lateinit var authTokenStore: AuthTokenStore

    @Before
    fun setUp() {
        `when`(preferenceDataSource.tokenPair).thenReturn(emptyFlow())
        authTokenStore = AuthTokenStore(
            preferenceDataSource = preferenceDataSource,
            externalScope = CoroutineScope(SupervisorJob() + testDispatcher),
            ioDispatcher = testDispatcher,
        )
    }

    @Test
    fun `does not persist refreshed tokens after logout clears a newer token generation`() = runTest(testDispatcher) {
        val refreshedTokenPair = TokenPair(
            accessToken = "new-access-token",
            refreshToken = "new-refresh-token",
        )
        val refreshGeneration = authTokenStore.updateCachedTokens(refreshedTokenPair)

        val logoutGeneration = authTokenStore.clearCachedTokens()
        val refreshedTokensPersisted = authTokenStore.persistTokensIfCurrent(
            tokenPair = refreshedTokenPair,
            generation = refreshGeneration,
        )
        val clearedTokensPersisted = authTokenStore.persistTokenClearIfCurrent(logoutGeneration)

        assertFalse(refreshedTokensPersisted)
        assertTrue(clearedTokensPersisted)
        assertNull(authTokenStore.cachedTokenPair)
        verify(preferenceDataSource, never()).saveTokens(
            refreshedTokenPair.accessToken,
            refreshedTokenPair.refreshToken,
        )
        verify(preferenceDataSource).clearTokens()
    }

    @Test
    fun `starts uninitialized then exposes loaded token pair consistently through cache and auth state`() = runTest(testDispatcher) {
        val storedTokenPair = TokenPair(
            accessToken = "stored-access-token",
            refreshToken = "stored-refresh-token",
        )
        val tokenPairFlow = MutableSharedFlow<TokenPair?>()
        val persistedAuthState = MutableStateFlow(
            AuthState(
                accessToken = "stale-persisted-access-token",
                isOnboardingFinished = true,
            ),
        )
        val initializingPreferenceDataSource: PreferenceDataSource = mock(PreferenceDataSource::class.java)
        `when`(initializingPreferenceDataSource.tokenPair).thenReturn(tokenPairFlow)
        `when`(initializingPreferenceDataSource.authState).thenReturn(persistedAuthState)
        val initializingTokenStore = AuthTokenStore(
            preferenceDataSource = initializingPreferenceDataSource,
            externalScope = CoroutineScope(SupervisorJob() + testDispatcher),
            ioDispatcher = testDispatcher,
        )
        val observedAuthStates = mutableListOf<AuthState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            initializingTokenStore.authState.collect(observedAuthStates::add)
        }

        assertFalse(initializingTokenStore.tokenState.value.isInitialized)
        assertNull(initializingTokenStore.cachedTokenPair)

        runCurrent()
        tokenPairFlow.emit(storedTokenPair)
        advanceUntilIdle()

        assertTrue(initializingTokenStore.tokenState.value.isInitialized)
        assertEquals(storedTokenPair, initializingTokenStore.cachedTokenPair)
        assertEquals(storedTokenPair.accessToken, observedAuthStates.last().accessToken)
        assertTrue(observedAuthStates.last().isOnboardingFinished)
        assertTrue(observedAuthStates.last().isInitialized)
    }
}
