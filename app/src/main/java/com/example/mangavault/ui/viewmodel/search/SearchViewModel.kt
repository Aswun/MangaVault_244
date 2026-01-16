package com.example.mangavault.ui.viewmodel.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangavault.data.remote.dto.JikanMangaDto
import com.example.mangavault.data.repository.LibraryRepository
import com.example.mangavault.data.repository.SearchRepository
import com.example.mangavault.util.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val libraryRepository: LibraryRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Idle)
    val state: StateFlow<SearchState> = _state

    // Helper untuk mengambil list hasil jika sukses agar bisa diakses oleh Detail Screen
    val results: StateFlow<List<JikanMangaDto>>
        get() = if (_state.value is SearchState.Success) {
            MutableStateFlow((_state.value as SearchState.Success).mangaList)
        } else {
            MutableStateFlow(emptyList())
        }

    fun search(query: String, context: Context) {
        if (query.isBlank()) return

        // REVISI: Cek koneksi internet SEBELUM memanggil API (REQ-SEA-06)
        if (!NetworkUtils.isInternetAvailable(context)) {
            _state.value = SearchState.Error("No internet connection. Please check your network.")
            return
        }

        viewModelScope.launch {
            _state.value = SearchState.Loading
            try {
                val result = searchRepository.searchManga(query)
                if (result.isEmpty()) {
                    _state.value = SearchState.Error("Manga not found.")
                } else {
                    _state.value = SearchState.Success(result)
                }
            } catch (e: HttpException) {
                // Handling Rate Limit (HTTP 429) - REQ-OTH-19
                if (e.code() == 429) {
                    _state.value = SearchState.Error("Too many requests. Please wait a moment before searching again.")
                } else {
                    _state.value = SearchState.Error("Server error: ${e.message()}")
                }
            } catch (e: IOException) {
                _state.value = SearchState.Error("Network error. Check your connection.")
            } catch (e: Exception) {
                _state.value = SearchState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    // REVISI: Menerima parameter status, rating, dan volume dari Dialog (REQ-SEA-07)
    fun saveToLibrary(
        manga: JikanMangaDto,
        status: String,
        rating: Int?,
        volumeOwned: Int
    ) {
        viewModelScope.launch {
            libraryRepository.saveManga(
                mangaId = manga.malId,
                title = manga.title,
                imageUrl = manga.images?.jpg?.imageUrl,
                status = status,            // Data dari input user
                volumeOwned = volumeOwned,  // Data dari input user
                rating = rating             // Data dari input user
            )
        }
    }
}