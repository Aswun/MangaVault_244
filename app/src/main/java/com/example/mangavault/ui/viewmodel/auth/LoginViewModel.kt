package com.example.mangavault.ui.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangavault.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel untuk menangani logika Autentikasi (Login).
 * Mengelola state UI dari layar Login dan berkomunikasi dengan AuthRepository.
 *
 * @param authRepository Repository yang menangani verifikasi kredensial user.
 */
class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    /**
     * Melakukan proses login dengan memvalidasi input dan memanggil repository.
     * Mengupdate [uiState] berdasarkan hasil operasi (Loading, Success, Error).
     *
     * @param username Nama pengguna.
     * @param password Kata sandi pengguna.
     */
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

    /**
     * Mereset state UI kembali ke Idle.
     * Berguna saat pengguna logout atau menavigasi keluar dari layar login
     * agar state sukses/error sebelumnya tidak tampil kembali.
     */
    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}