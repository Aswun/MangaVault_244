package com.example.mangavault.ui.viewmodel.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangavault.data.local.entity.MangaEntity
import com.example.mangavault.data.repository.LibraryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(private val repository: LibraryRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<LibraryState>(LibraryState.Loading)
    val uiState: StateFlow<LibraryState> = _uiState.asStateFlow()

    init {
        loadLibrary()
    }

    private fun loadLibrary() {
        viewModelScope.launch {
            repository.getUserManga().collect { list ->
                _uiState.value = LibraryState.Success(list)
            }
        }
    }

    fun updateManga(manga: MangaEntity) {
        viewModelScope.launch {
            repository.updateManga(
                manga.mangaId,
                manga.userId,
                manga.status,
                manga.rating,
                manga.volumeOwned
            )
        }
    }

    fun deleteManga(mangaId: Int) {
        viewModelScope.launch {
            repository.deleteManga(mangaId)
        }
    }
}