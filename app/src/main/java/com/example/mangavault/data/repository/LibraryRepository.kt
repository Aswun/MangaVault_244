package com.example.mangavault.data.repository

import com.example.mangavault.data.local.dao.MangaDao
import com.example.mangavault.data.local.entity.MangaEntity
import com.example.mangavault.datastore.SessionPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class LibraryRepository(
    private val mangaDao: MangaDao,
    private val sessionPreferences: SessionPreferences
) {

    // Ambil data manga KHUSUS untuk user yang sedang login
    suspend fun getUserManga(): Flow<List<MangaEntity>> {
        val userId = sessionPreferences.userId.first()
        return if (userId != null) {
            mangaDao.getMangaByUser(userId)
        } else {
            flowOf(emptyList()) // Return kosong jika tidak ada user
        }
    }

    // Simpan manga dengan menyertakan userId
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
                userId = userId, // FIX: UserId diambil dari session
                title = title,
                imageUrl = imageUrl,
                status = status,
                volumeOwned = volumeOwned,
                rating = rating
            )
            mangaDao.upsertManga(manga)
        }
    }

    suspend fun deleteManga(mangaId: Int) {
        val userId = sessionPreferences.userId.first()
        if (userId != null) {
            // Kita perlu hapus berdasarkan composite key (mangaId + userId)
            // Asumsi di DAO ada method deleteMangaByIds(mangaId, userId)
            // Jika di DAO anda hanya ada delete(entity), maka:
            val manga = mangaDao.getMangaByIdAndUser(mangaId, userId)
            if (manga != null) {
                mangaDao.deleteManga(manga)
            }
        }
    }

    suspend fun updateManga(manga: MangaEntity) {
        // Pastikan manga yang diupdate milik user yang login
        val userId = sessionPreferences.userId.first()
        if (userId != null && manga.userId == userId) {
            mangaDao.upsertManga(manga)
        }
    }
}