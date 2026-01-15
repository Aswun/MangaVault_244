package com.example.mangavault.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "session_prefs")

class SessionPreferences(
    private val context: Context
) {

    companion object {
        private val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val KEY_USER_ID = intPreferencesKey("user_id")
    }

    val isLoggedIn: Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[KEY_IS_LOGGED_IN] ?: false
        }

    val userId: Flow<Int?> =
        context.dataStore.data.map { prefs ->
            prefs[KEY_USER_ID]
        }

    suspend fun saveSession(userId: Int) {
        context.dataStore.edit { prefs ->
            prefs[KEY_IS_LOGGED_IN] = true
            prefs[KEY_USER_ID] = userId
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
