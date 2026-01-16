package com.example.mangavault.ui.viewmodel.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangavault.data.local.entity.MangaEntity
import com.example.mangavault.data.repository.LibraryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Enum untuk Opsi Sorting
enum class SortOption {
    TITLE, STATUS, RATING
}

class LibraryViewModel(private val repository: LibraryRepository) : ViewModel() {

    // State untuk Sort Option
    private val _sortOption = MutableStateFlow(SortOption.TITLE)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    private val _uiState = MutableStateFlow<LibraryState>(LibraryState.Loading)
    val uiState: StateFlow<LibraryState> = _uiState.asStateFlow()

    init {
        loadLibrary()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadLibrary() {
        viewModelScope.launch {
            // Mengamati perubahan sortOption dan mengambil data ulang secara reaktif
            _sortOption.flatMapLatest { sort ->
                repository.getUserManga(sort)
            }.collect { list ->
                if (list.isEmpty()) {
                    _uiState.value = LibraryState.Empty
                } else {
                    _uiState.value = LibraryState.Success(list)
                }
            }
        }
    }

    fun updateSortOption(option: SortOption) {
        _sortOption.value = option
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