package com.example.mangavault.ui.viewmodel.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangavault.data.local.entity.MangaEntity
import com.example.mangavault.data.repository.LibraryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val libraryRepository: LibraryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LibraryState>(LibraryState.Loading)
    val uiState: StateFlow<LibraryState> = _uiState.asStateFlow()

    init {
        loadUserManga()
    }

    fun loadUserManga() {
        viewModelScope.launch {
            _uiState.value = LibraryState.Loading
            try {
                libraryRepository.getUserManga().collect { mangaList ->
                    if (mangaList.isEmpty()) {
                        _uiState.value = LibraryState.Empty
                    } else {
                        _uiState.value = LibraryState.Success(mangaList)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = LibraryState.Error("Gagal memuat library: ${e.message}")
            }
        }
    }

    fun deleteManga(mangaId: Int) {
        viewModelScope.launch {
            libraryRepository.deleteManga(mangaId)
            loadUserManga()
        }
    }

    fun updateManga(manga: MangaEntity) {
        viewModelScope.launch {
            libraryRepository.updateManga(manga)
            loadUserManga()
        }
    }

    // FUNGSI LOGOUT TELAH DIHAPUS (Pindah ke SettingsViewModel)
}