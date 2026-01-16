package com.example.mangavault.ui.viewmodel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangavault.datastore.SessionPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val sessionPreferences: SessionPreferences
) : ViewModel() {

    // Mengamati status Dark Mode
    val isDarkMode = sessionPreferences.isDarkMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // Fungsi ganti tema
    fun updateTheme(isDark: Boolean) {
        viewModelScope.launch {
            sessionPreferences.saveTheme(isDark)
        }
    }

    // Fungsi logout
    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            sessionPreferences.clearSession()
            onLogoutSuccess()
        }
    }
}