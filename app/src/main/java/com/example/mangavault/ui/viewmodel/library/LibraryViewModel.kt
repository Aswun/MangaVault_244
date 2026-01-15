package com.example.mangavault.ui.viewmodel.library

import androidx.lifecycle.ViewModel
import com.example.mangavault.data.repository.LibraryRepository

class LibraryViewModel(
    private val repository: LibraryRepository
) : ViewModel()

fun addOrUpdateManga(
    mangaId: Int,
    title: String,
    imageUrl: String?,
    status: String,
    volumeOwned: Int,
    rating: Int?
) {
    viewModelScope.launch {
        repository.saveManga(
            mangaId,
            title,
            imageUrl,
            status,
            volumeOwned,
            rating
        )
    }
}

fun deleteManga(mangaId: Int) {
    viewModelScope.launch {
        repository.deleteManga(mangaId)
    }
}

private val _selectedManga =
    MutableStateFlow<MangaEntity?>(null)
val selectedManga: StateFlow<MangaEntity?> = _selectedManga

fun selectManga(manga: MangaEntity) {
    _selectedManga.value = manga
}

fun clearSelection() {
    _selectedManga.value = null
}

fun updateManga(
    mangaId: Int,
    status: String,
    rating: Int?,
    volumeOwned: Int
) {
    viewModelScope.launch {
        repository.updateManga(
            mangaId = mangaId,
            status = status,
            rating = rating,
            volumeOwned = volumeOwned
        )
        clearSelection()
    }
}

private val _mangaToDelete =
    MutableStateFlow<MangaEntity?>(null)
val mangaToDelete: StateFlow<MangaEntity?> = _mangaToDelete

fun requestDelete(manga: MangaEntity) {
    _mangaToDelete.value = manga
}

fun cancelDelete() {
    _mangaToDelete.value = null
}

fun confirmDelete() {
    val manga = _mangaToDelete.value ?: return

    viewModelScope.launch {
        repository.deleteManga(manga.mangaId)
        _mangaToDelete.value = null
    }
}
