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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel Global untuk MainActivity.
 * Menangani logika inisialisasi aplikasi seperti:
 * 1. Pengecekan sesi login untuk menentukan rute awal (Start Destination).
 * 2. Menyediakan state Dark Mode untuk seluruh aplikasi.
 * 3. Mengelola state loading (Splash Screen).
 */
class MainViewModel(
    private val sessionPreferences: SessionPreferences
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination.asStateFlow()

    // Mengubah Flow preferensi menjadi StateFlow agar UI dapat langsung merender tema awal
    val isDarkMode: StateFlow<Boolean> = sessionPreferences.isDarkMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    init {
        observeSession()
    }

    private fun observeSession() {
        viewModelScope.launch {
            // Cek status login sekali di awal untuk routing
            val isLoggedIn = sessionPreferences.isLoggedIn.first()

            _startDestination.value = if (isLoggedIn) {
                NavRoute.Library.route
            } else {
                NavRoute.Login.route
            }

            // Delay buatan agar splash screen tidak terlalu cepat hilang (estetika)
            delay(500)
            _isLoading.value = false
        }
    }
}