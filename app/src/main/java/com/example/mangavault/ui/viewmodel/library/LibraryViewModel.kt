package com.example.mangavault.ui.viewmodel.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangavault.data.local.entity.MangaEntity
import com.example.mangavault.data.repository.LibraryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Pilihan opsi pengurutan daftar manga di Library.
 */
enum class SortOption {
    TITLE, STATUS, RATING
}

/**
 * ViewModel untuk mengelola Library (Koleksi Manga Lokal).
 * Menangani pengambilan data dari Room Database, sorting, update, dan penghapusan data.
 *
 * @param repository Repository library untuk operasi database.
 */
class LibraryViewModel(private val repository: LibraryRepository) : ViewModel() {

    // State untuk menampung opsi sorting yang aktif saat ini
    private val _sortOption = MutableStateFlow(SortOption.TITLE)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    private val _uiState = MutableStateFlow<LibraryState>(LibraryState.Loading)
    val uiState: StateFlow<LibraryState> = _uiState.asStateFlow()

    init {
        loadLibrary()
    }

    /**
     * Memuat daftar manga dari database secara reaktif.
     * Fungsi ini akan memantau perubahan pada [sortOption] dan otomatis mengambil ulang data
     * sesuai urutan yang dipilih.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadLibrary() {
        viewModelScope.launch {
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

    /**
     * Mengubah kriteria pengurutan daftar manga.
     *
     * @param option Opsi sorting baru (Judul, Status, atau Rating).
     */
    fun updateSortOption(option: SortOption) {
        _sortOption.value = option
    }

    /**
     * Memperbarui metadata manga (status, rating, volume) di database.
     *
     * @param manga Objek manga dengan data terbaru.
     */
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

    /**
     * Menghapus manga dari koleksi library pengguna.
     *
     * @param mangaId ID unik manga yang akan dihapus.
     */
    fun deleteManga(mangaId: Int) {
        viewModelScope.launch {
            repository.deleteManga(mangaId)
        }
    }
}