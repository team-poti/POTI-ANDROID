package com.poti.android.core.permission.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.poti.android.core.common.util.suspendRunCatching
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.permissionDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "permission_prefs",
)

@Singleton
class PermissionDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    suspend fun hasShownSystemPermissionDialog(): Boolean = suspendRunCatching {
        context.permissionDataStore.data.first()[HAS_SHOWN_SYSTEM_PERMISSION_DIALOG_KEY] == true
    }.getOrDefault(true)

    suspend fun markSystemPermissionDialogShown() {
        suspendRunCatching {
            context.permissionDataStore.edit { prefs ->
                prefs[HAS_SHOWN_SYSTEM_PERMISSION_DIALOG_KEY] = true
            }
        }
    }

    private companion object {
        val HAS_SHOWN_SYSTEM_PERMISSION_DIALOG_KEY =
            booleanPreferencesKey("HAS_SHOWN_SYSTEM_PERMISSION_DIALOG")
    }
}
