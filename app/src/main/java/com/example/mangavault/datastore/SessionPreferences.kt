package com.example.mangavault.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session_prefs")

class SessionPreferences(private val context: Context) {

    companion object {
        private val USER_ID = intPreferencesKey("user_id")
        private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    }

    // Mendapatkan User ID (Return null jika belum login)
    val userId: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            val id = preferences[USER_ID]
            if (id == null || id == -1) null else id
        }

    // Cek status login berdasarkan ada/tidaknya User ID
    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            (preferences[USER_ID] ?: -1) != -1
        }

    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_DARK_MODE] ?: false
        }

    // Simpan session saat login berhasil
    suspend fun saveSession(id: Int) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = id
        }
    }

    // Simpan preferensi tema
    suspend fun saveTheme(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = isDarkMode
        }
    }

    // Hapus session saat logout
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_ID)
        }
    }
}