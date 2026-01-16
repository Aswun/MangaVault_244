package com.example.mangavault.ui.viewmodel.search

import com.example.mangavault.data.remote.dto.JikanMangaDto

/**
 * Sealed interface untuk merepresentasikan state pada layar Pencarian (Search).
 */
sealed interface SearchState {
    /** State awal saat belum ada pencarian dilakukan. */
    object Idle : SearchState

    /** State saat proses pencarian sedang berlangsung. */
    object Loading : SearchState

    /**
     * State saat pencarian berhasil.
     * @param mangaList Daftar manga hasil pencarian dari API.
     */
    data class Success(val mangaList: List<JikanMangaDto>) : SearchState

    /**
     * State saat terjadi error selama pencarian.
     * @param message Pesan error yang ditampilkan ke pengguna.
     */
    data class Error(val message: String) : SearchState
}