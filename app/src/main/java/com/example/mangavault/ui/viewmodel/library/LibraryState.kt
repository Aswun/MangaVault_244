package com.example.mangavault.ui.viewmodel.library

import com.example.mangavault.data.local.entity.MangaEntity

sealed interface LibraryState {
    data object Loading : LibraryState
    data object Empty : LibraryState

    // State sukses membawa list data manga
    data class Success(val mangaList: List<MangaEntity>) : LibraryState

    // State error membawa pesan error
    data class Error(val message: String) : LibraryState
}