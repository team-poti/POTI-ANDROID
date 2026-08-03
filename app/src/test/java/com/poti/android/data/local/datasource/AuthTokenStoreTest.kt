package com.poti.android.data.local.datasource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

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
}
