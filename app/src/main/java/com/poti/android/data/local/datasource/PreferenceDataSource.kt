package com.poti.android.data.local.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("ACCESS_TOKEN")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("REFRESH_TOKEN")
    }

    suspend fun saveAccessToken(accessToken: String) = dataStore.edit { prefs ->
        prefs[ACCESS_TOKEN_KEY] = accessToken
    }

    suspend fun saveRefreshToken(refreshToken: String) = dataStore.edit { prefs ->
        prefs[REFRESH_TOKEN_KEY] = refreshToken
    }

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
    ) = dataStore.edit { prefs ->
        prefs[ACCESS_TOKEN_KEY] = accessToken
        prefs[REFRESH_TOKEN_KEY] = refreshToken
    }

    val accessToken: Flow<String?> = dataStore.data.map { prefs ->
        prefs[ACCESS_TOKEN_KEY]
    }

    val refreshToken: Flow<String?> = dataStore.data.map { prefs ->
        prefs[REFRESH_TOKEN_KEY]
    }

    suspend fun clear() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
