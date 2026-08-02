package com.poti.android.data.local.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.poti.android.domain.model.auth.AuthState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("ACCESS_TOKEN")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("REFRESH_TOKEN")
        private val IS_ONBOARDING_FINISHED_KEY = booleanPreferencesKey("IS_ONBOARDING_FINISHED")
    }

    val tokenPair: Flow<TokenPair?> = dataStore.data.map { prefs ->
        val accessToken = prefs[ACCESS_TOKEN_KEY]
        val refreshToken = prefs[REFRESH_TOKEN_KEY]

        if (accessToken != null && refreshToken != null) {
            TokenPair(
                accessToken = accessToken,
                refreshToken = refreshToken,
            )
        } else {
            null
        }
    }

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
    ) {
        dataStore.edit { prefs ->
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
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
            prefs.remove(REFRESH_TOKEN_KEY)
        }
    }

    suspend fun clearAll() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
