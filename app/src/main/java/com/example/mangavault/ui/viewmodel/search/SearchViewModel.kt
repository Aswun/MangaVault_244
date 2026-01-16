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

/**
 * ViewModel untuk menangani fitur Pencarian Manga.
 * Bertanggung jawab melakukan request ke Jikan API dan menyimpan manga pilihan ke library lokal.
 *
 * @param searchRepository Repository untuk akses data API.
 * @param libraryRepository Repository untuk menyimpan data ke Room Database.
 */
class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val libraryRepository: LibraryRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Idle)
    val state: StateFlow<SearchState> = _state

    /**
     * Helper property untuk menyediakan akses ke hasil pencarian terakhir.
     * Digunakan oleh Detail Screen untuk mengambil data manga tanpa request ulang.
     */
    val results: StateFlow<List<JikanMangaDto>>
        get() = if (_state.value is SearchState.Success) {
            MutableStateFlow((_state.value as SearchState.Success).mangaList)
        } else {
            MutableStateFlow(emptyList())
        }

    /**
     * Mencari manga berdasarkan query.
     * Melakukan pengecekan koneksi internet terlebih dahulu sebelum request API.
     * Menangani berbagai jenis error seperti koneksi putus atau Rate Limit (HTTP 429).
     *
     * @param query Kata kunci judul manga.
     * @param context Context Android untuk pengecekan koneksi jaringan.
     */
    fun search(query: String, context: Context) {
        if (query.isBlank()) return

        // Validasi koneksi internet (REQ-SEA-06)
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
                // Menangani Rate Limit API (REQ-OTH-19)
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

    /**
     * Menyimpan manga terpilih ke koleksi lokal pengguna.
     * Data dari API digabung dengan metadata (status, rating, volume) yang diinput user.
     *
     * @param manga Objek data manga dari API.
     * @param status Status baca (Reading/Completed/Plan to Read).
     * @param rating Penilaian pengguna (opsional).
     * @param volumeOwned Jumlah volume yang dimiliki.
     */
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
                status = status,
                volumeOwned = volumeOwned,
                rating = rating
            )
        }
    }
}