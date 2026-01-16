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
    val isDarkMode: StateFlow<Boolean> = sessionPreferences.isDarkMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // Status Login (Ditambahkan untuk kebutuhan UI Settings)
    val isLoggedIn: StateFlow<Boolean> = sessionPreferences.isLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            sessionPreferences.saveTheme(enabled)
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionPreferences.clearSession()
            // Navigasi akan ditangani oleh MainViewModel yang mengamati session
        }
    }
}