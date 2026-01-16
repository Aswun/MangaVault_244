package com.example.mangavault.ui.viewmodel.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangavault.data.local.entity.MangaEntity
import com.example.mangavault.data.repository.LibraryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val repository: LibraryRepository
) : ViewModel() {

    // --- State untuk List Manga ---
    // Mengambil data flow dari repository dan mengubahnya menjadi StateFlow atau dikonsumsi di UI
    // Karena repository.getUserManga() return Flow, kita bisa mengeksposnya langsung atau membungkusnya.
    // Di sini saya perbaiki agar sesuai dengan penggunaan di LibraryScreen (collectAsState)

    // Kita butuh variabel untuk menampung list manga agar bisa diobservasi oleh UI
    private val _mangaList = MutableStateFlow<List<MangaEntity>>(emptyList())
    val mangaList: StateFlow<List<MangaEntity>> = _mangaList

    init {
        loadManga()
    }

    private fun loadManga() {
        viewModelScope.launch {
            try {
                repository.getUserManga().collect { list ->
                    _mangaList.value = list
                }
            } catch (e: Exception) {
                // Handle error jika user belum login (sesuai logic repository)
            }
        }
    }

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

    private val _selectedManga = MutableStateFlow<MangaEntity?>(null)
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

    private val _mangaToDelete = MutableStateFlow<MangaEntity?>(null)
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

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}