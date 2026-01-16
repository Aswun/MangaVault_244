package com.example.mangavault.data.repository

import com.example.mangavault.data.local.dao.MangaDao
import com.example.mangavault.data.local.entity.MangaEntity
import com.example.mangavault.datastore.SessionPreferences
import com.example.mangavault.ui.viewmodel.library.SortOption
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

/**
 * Repository untuk mengelola data koleksi manga (Library).
 * Memastikan data yang diakses hanya milik pengguna yang sedang login.
 */
class LibraryRepository(
    private val mangaDao: MangaDao,
    private val sessionPreferences: SessionPreferences
) {
    /**
     * Mengambil daftar manga milik user yang sedang login secara reaktif.
     * Menggunakan [flatMapLatest] untuk memantau perubahan userId (misal saat login/logout),
     * sehingga data yang dikembalikan selalu sinkron dengan user aktif.
     *
     * @param sortOption Kriteria pengurutan (Judul, Status, atau Rating).
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getUserManga(sortOption: SortOption): Flow<List<MangaEntity>> {
        return sessionPreferences.userId.flatMapLatest { userId ->
            if (userId == null) {
                flowOf(emptyList())
            } else {
                when (sortOption) {
                    SortOption.TITLE -> mangaDao.getMangaByTitle(userId)
                    SortOption.STATUS -> mangaDao.getMangaByStatus(userId)
                    SortOption.RATING -> mangaDao.getMangaByRating(userId)
                }
            }
        }
    }

    /**
     * Menyimpan manga baru ke koleksi user yang sedang login.
     */
    suspend fun saveManga(
        mangaId: Int,
        title: String,
        imageUrl: String?,
        status: String,
        volumeOwned: Int,
        rating: Int?
    ) {
        val userId = sessionPreferences.userId.first()
        if (userId != null) {
            val manga = MangaEntity(
                mangaId = mangaId,
                userId = userId,
                title = title,
                imageUrl = imageUrl,
                status = status,
                volumeOwned = volumeOwned,
                rating = rating
            )
            mangaDao.upsertManga(manga)
        }
    }

    /**
     * Menghapus manga dari koleksi user.
     */
    suspend fun deleteManga(mangaId: Int) {
        val userId = sessionPreferences.userId.first()
        if (userId != null) {
            mangaDao.deleteManga(mangaId, userId)
        }
    }

    /**
     * Memperbarui metadata manga milik user.
     * Melakukan validasi userId terlebih dahulu untuk keamanan data.
     */
    suspend fun updateManga(
        mangaId: Int,
        userId: Int,
        status: String,
        rating: Int?,
        volumeOwned: Int
    ) {
        val currentUserId = sessionPreferences.userId.first()
        // Pastikan hanya pemilik data yang bisa melakukan update
        if (currentUserId != null && currentUserId == userId) {
            mangaDao.updateManga(mangaId, userId, status, rating, volumeOwned)
        }
    }
}