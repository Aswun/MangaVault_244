package com.example.mangavault.ui.viewmodel.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangavault.data.remote.dto.JikanMangaDto
import com.example.mangavault.data.repository.LibraryRepository
import com.example.mangavault.data.repository.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val libraryRepository: LibraryRepository
) : ViewModel() {

    private val _results =
        MutableStateFlow<List<JikanMangaDto>>(emptyList())
    val results: StateFlow<List<JikanMangaDto>> = _results

    fun search(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _results.value = searchRepository.searchManga(query)
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
