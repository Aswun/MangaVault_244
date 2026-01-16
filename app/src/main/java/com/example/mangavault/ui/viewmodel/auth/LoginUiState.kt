package com.example.mangavault.ui.viewmodel.auth

/**
 * Sealed class untuk merepresentasikan state pada layar Login.
 */
sealed class LoginUiState {
    /** State awal atau diam. */
    data object Idle : LoginUiState()

    /** State saat proses login sedang berjalan. */
    data object Loading : LoginUiState()

    /** State saat login berhasil. */
    data object Success : LoginUiState()

    /**
     * State saat login gagal.
     * @param message Pesan kesalahan (misal: password salah).
     */
    data class Error(val message: String) : LoginUiState()
}