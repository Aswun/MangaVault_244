package com.example.mangavault.ui.viewmodel.search

import com.example.mangavault.data.remote.dto.JikanMangaDto

sealed interface SearchState {
    object Idle : SearchState
    object Loading : SearchState
    data class Success(val mangaList: List<JikanMangaDto>) : SearchState
    data class Error(val message: String) : SearchState
}