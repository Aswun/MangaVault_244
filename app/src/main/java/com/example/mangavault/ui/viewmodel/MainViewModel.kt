package com.example.mangavault.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangavault.datastore.SessionPreferences
import com.example.mangavault.ui.navigation.NavRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionPreferences: SessionPreferences
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Default start destination
    private val _startDestination = MutableStateFlow(NavRoute.Login.route)
    val startDestination: StateFlow<String> = _startDestination.asStateFlow()

    // Tetap diperlukan untuk MainActivity (Theme observer)
    val isDarkMode = sessionPreferences.isDarkMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    init {
        observeSession()
    }

    private fun observeSession() {
        // Pemantauan sesi secara real-time (Memperbaiki Bug Logout kembali ke Library)
        viewModelScope.launch {
            sessionPreferences.isLoggedIn.collect { isLoggedIn ->
                _startDestination.value = if (isLoggedIn) {
                    NavRoute.Library.route
                } else {
                    NavRoute.Login.route
                }
            }
        }

        // Logika Splash Screen terpisah
        viewModelScope.launch {
            delay(1000)
            _isLoading.value = false
        }
    }
}