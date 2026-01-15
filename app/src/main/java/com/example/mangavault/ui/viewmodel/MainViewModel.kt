package com.example.mangavault.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangavault.datastore.SessionPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionPreferences: SessionPreferences
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination

    init {
        viewModelScope.launch {
            sessionPreferences.isLoggedIn.collect { loggedIn ->
                _startDestination.value =
                    if (loggedIn) {
                        "library"
                    } else {
                        "login"
                    }
            }
        }
    }
}
