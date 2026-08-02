package com.poti.android.data.local.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.poti.android.di.ApplicationScope
import com.poti.android.di.IoDispatcher
import com.poti.android.domain.model.auth.AuthState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.set

data class TokenPair(
    val accessToken: String,
    val refreshToken: String,
)

class PreferenceDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @ApplicationScope private val externalScope: CoroutineScope,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("ACCESS_TOKEN")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("REFRESH_TOKEN")
        private val IS_ONBOARDING_FINISHED_KEY = booleanPreferencesKey("IS_ONBOARDING_FINISHED")
    }

    val accessToken: Flow<String?> = dataStore.data.map { prefs ->
        prefs[ACCESS_TOKEN_KEY]
    }

    val refreshToken: Flow<String?> = dataStore.data.map { prefs ->
        prefs[REFRESH_TOKEN_KEY]
    }

    @Volatile
    private var _cachedAccessToken: String? = null
    val cachedAccessToken: String?
        get() = _cachedAccessToken

    @Volatile
    private var _cachedRefreshToken: String? = null
    val cachedRefreshToken: String?
        get() = _cachedRefreshToken

    @Volatile
    private var _cachedTokenPair: TokenPair? = null
    val cachedTokenPair: TokenPair?
        get() = _cachedTokenPair

    init {
        externalScope.launch(ioDispatcher) {
            dataStore.data.collect { prefs ->
                val accessToken = prefs[ACCESS_TOKEN_KEY]
                val refreshToken = prefs[REFRESH_TOKEN_KEY]

                _cachedAccessToken = accessToken
                _cachedRefreshToken = refreshToken
                _cachedTokenPair = if (accessToken != null && refreshToken != null) {
                    TokenPair(
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                    )
                } else {
                    null
                }
            }
        }
    }

    suspend fun saveAccessToken(accessToken: String) = dataStore.edit { prefs ->
        prefs[ACCESS_TOKEN_KEY] = accessToken
    }

    suspend fun saveRefreshToken(refreshToken: String) = dataStore.edit { prefs ->
        Timber.d("saveRefreshToken 호출됨")
        prefs[REFRESH_TOKEN_KEY] = refreshToken
    }

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
    ) {
        _cachedAccessToken = accessToken
        _cachedRefreshToken = refreshToken
        _cachedTokenPair = TokenPair(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )

        dataStore.edit { prefs ->
            Timber.d("saveTokens 호출됨 - Access: ${accessToken.take(5)}... Refresh: ${refreshToken.take(5)}...")
            prefs[ACCESS_TOKEN_KEY] = accessToken
            prefs[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    suspend fun saveOnboardingState(isFinished: Boolean) = dataStore.edit { prefs ->
        prefs[IS_ONBOARDING_FINISHED_KEY] = isFinished
    }

    val authState: Flow<AuthState> = dataStore.data.map { prefs ->
        AuthState(
            accessToken = prefs[ACCESS_TOKEN_KEY],
            isOnboardingFinished = prefs[IS_ONBOARDING_FINISHED_KEY] == true,
        )
    }

    suspend fun clearTokens() {
        _cachedAccessToken = null
        _cachedRefreshToken = null
        _cachedTokenPair = null

        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
            prefs.remove(REFRESH_TOKEN_KEY)
        }
    }

    suspend fun clearAll() {
        _cachedAccessToken = null
        _cachedRefreshToken = null
        _cachedTokenPair = null

        dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
