package com.poti.android.data.local.datasource

import androidx.datastore.core.DataStore
import java.util.prefs.Preferences

class PreferenceDataSource(
    private val dataStore: DataStore<Preferences>,
) {
}
