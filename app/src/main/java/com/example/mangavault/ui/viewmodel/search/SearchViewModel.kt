package com.example.mangavault.ui.viewmodel.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangavault.data.remote.dto.JikanMangaDto
import com.example.mangavault.data.repository.LibraryRepository
import com.example.mangavault.data.repository.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val libraryRepository: LibraryRepository
) : ViewModel() {

    // Menggunakan SearchState alih-alih List biasa
    private val _state = MutableStateFlow<SearchState>(SearchState.Idle)
    val state: StateFlow<SearchState> = _state

    // Kita tetap simpan results terakhir untuk akses detail screen jika perlu
    val results: StateFlow<List<JikanMangaDto>>
        get() = if (_state.value is SearchState.Success) {
            MutableStateFlow((_state.value as SearchState.Success).mangaList)
        } else {
            MutableStateFlow(emptyList())
        }

    fun search(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _state.value = SearchState.Loading
            try {
                val result = searchRepository.searchManga(query)
                if (result.isEmpty()) {
                    _state.value = SearchState.Error("Manga not found.")
                } else {
                    _state.value = SearchState.Success(result)
                }
            } catch (e: IOException) {
                _state.value = SearchState.Error("Network error. Check your connection.")
            } catch (e: Exception) {
                _state.value = SearchState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun saveToLibrary(manga: JikanMangaDto) {
        viewModelScope.launch {
            libraryRepository.saveManga(
                mangaId = manga.malId,
                title = manga.title,
                imageUrl = manga.images?.jpg?.imageUrl,
                status = "Plan to Read",
                volumeOwned = 0,
                rating = null
            )
        }
    }
}