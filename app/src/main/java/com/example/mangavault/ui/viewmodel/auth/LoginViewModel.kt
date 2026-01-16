package com.example.mangavault.ui.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangavault.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(
        username: String,
        password: String
    ) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value =
                LoginUiState.Error("Username dan password wajib diisi")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            authRepository.login(username, password)
                .onSuccess {
                    _uiState.value = LoginUiState.Success
                }
                .onFailure {
                    _uiState.value =
                        LoginUiState.Error(it.message ?: "Login gagal")
                }
        }
    }

    // PERBAIKAN: Fungsi untuk mereset state saat user logout atau kembali ke login screen
    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}