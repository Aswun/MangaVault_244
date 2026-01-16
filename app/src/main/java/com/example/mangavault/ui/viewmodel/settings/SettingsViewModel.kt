package com.example.mangavault.ui.viewmodel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangavault.datastore.SessionPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val sessionPreferences: SessionPreferences
) : ViewModel() {

    // Status Dark Mode
    val isDarkMode = sessionPreferences.isDarkMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // PERBAIKAN: Menambahkan status isLoggedIn agar UI bisa berubah dinamis
    val isLoggedIn: StateFlow<Boolean> = sessionPreferences.isLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun updateTheme(isDark: Boolean) {
        viewModelScope.launch {
            sessionPreferences.saveTheme(isDark)
        }
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            sessionPreferences.clearSession()
            onLogoutSuccess()
        }
    }
}