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

class MainViewModel(
    private val sessionPreferences: SessionPreferences
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination.asStateFlow()

    // PERBAIKAN: Menggunakan stateIn untuk mengubah Flow menjadi StateFlow
    // Ini memperbaiki error "No value passed for parameter 'initial'" di MainActivity
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
            // Mengambil status login sekali saja (first) untuk inisialisasi awal
            val isLoggedIn = sessionPreferences.isLoggedIn.first()

            _startDestination.value = if (isLoggedIn) {
                NavRoute.Library.route
            } else {
                NavRoute.Login.route
            }

            // Simulasi delay splash screen
            delay(500)
            _isLoading.value = false
        }
    }
}