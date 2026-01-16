package com.example.mangavault.ui.viewmodel.library

import com.example.mangavault.data.local.entity.MangaEntity

/**
 * Sealed interface untuk merepresentasikan state pada layar Library.
 */
sealed interface LibraryState {
    /** State saat data library sedang dimuat. */
    data object Loading : LibraryState

    /** State saat library kosong (user belum menyimpan manga). */
    data object Empty : LibraryState

    /**
     * State saat data library berhasil dimuat.
     * @param mangaList Daftar manga yang tersimpan di database lokal.
     */
    data class Success(val mangaList: List<MangaEntity>) : LibraryState

    /**
     * State saat terjadi kesalahan memuat data library.
     * @param message Pesan error.
     */
    data class Error(val message: String) : LibraryState
}