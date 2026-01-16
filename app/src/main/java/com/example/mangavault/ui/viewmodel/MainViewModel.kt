package com.example.mangavault.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangavault.datastore.SessionPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionPreferences: SessionPreferences
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination

    // Mengambil state tema dari DataStore dan mengubahnya menjadi StateFlow
    val isDarkMode: StateFlow<Boolean> = sessionPreferences.isDarkMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    init {
        viewModelScope.launch {
            sessionPreferences.isLoggedIn.collect { loggedIn ->
                _startDestination.value = if (loggedIn) "library" else "login"
            }
        }
    }

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            sessionPreferences.saveTheme(isDark)
        }
    }
}