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

/**
 * Wrapper class untuk mengelola penyimpanan preferensi lokal menggunakan DataStore.
 * Menyimpan data sesi pengguna (UserID) dan preferensi tema (Dark/Light).
 */
class SessionPreferences(private val context: Context) {

    companion object {
        private val USER_ID = intPreferencesKey("user_id")
        private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    }

    /**
     * Mendapatkan ID pengguna yang sedang login.
     * Mengembalikan null jika belum ada user yang login.
     */
    val userId: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            val id = preferences[USER_ID]
            if (id == null || id == -1) null else id
        }

    /**
     * Flow boolean yang menunjukkan apakah ada user yang sedang login.
     */
    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            (preferences[USER_ID] ?: -1) != -1
        }

    /**
     * Preferensi tema aplikasi (True = Dark Mode).
     */
    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_DARK_MODE] ?: false
        }

    /**
     * Menyimpan sesi login pengguna.
     */
    suspend fun saveSession(id: Int) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = id
        }
    }

    /**
     * Menyimpan pilihan tema pengguna.
     */
    suspend fun saveTheme(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = isDarkMode
        }
    }

    /**
     * Menghapus sesi pengguna (Logout).
     */
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_ID)
        }
    }
}