package com.example.mangavault.ui.viewmodel.library

import com.example.mangavault.data.local.entity.MangaEntity

sealed interface LibraryState {
    object Loading : LibraryState
    data class Success(val mangaList: List<MangaEntity>) : LibraryState
    object Empty : LibraryState
    data class Error(val message: String) : LibraryState
}