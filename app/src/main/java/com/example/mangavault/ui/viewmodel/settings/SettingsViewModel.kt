package com.example.mangavault.ui.viewmodel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangavault.datastore.SessionPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel untuk layar Pengaturan (Settings).
 * Mengelola preferensi pengguna seperti tema aplikasi (Dark/Light Mode)
 * dan status sesi login.
 *
 * @param sessionPreferences DataStore wrapper untuk menyimpan preferensi key-value.
 */
class SettingsViewModel(
    private val sessionPreferences: SessionPreferences
) : ViewModel() {

    /**
     * Flow yang memantau status Dark Mode secara real-time.
     */
    val isDarkMode = sessionPreferences.isDarkMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    /**
     * Flow yang memantau status Login pengguna.
     * Digunakan untuk menyesuaikan tampilan UI (misalnya tombol Login vs Logout).
     */
    val isLoggedIn: StateFlow<Boolean> = sessionPreferences.isLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    /**
     * Mengubah preferensi tema aplikasi.
     *
     * @param isDark True untuk mengaktifkan Dark Mode, False untuk Light Mode.
     */
    fun updateTheme(isDark: Boolean) {
        viewModelScope.launch {
            sessionPreferences.saveTheme(isDark)
        }
    }

    /**
     * Melakukan proses logout dengan menghapus data sesi.
     *
     * @param onLogoutSuccess Callback yang dijalankan setelah data sesi berhasil dihapus.
     */
    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            sessionPreferences.clearSession()
            onLogoutSuccess()
        }
    }
}