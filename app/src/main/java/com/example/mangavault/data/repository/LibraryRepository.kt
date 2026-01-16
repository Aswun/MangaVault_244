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
    suspend fun getUserManga(): Flow<List<MangaEntity>> {
        val userId = sessionPreferences.userId.first()
        return if (userId != null) {
            mangaDao.getMangaByUser(userId)
        } else {
            flowOf(emptyList())
        }
    }

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

    suspend fun deleteManga(mangaId: Int) {
        val userId = sessionPreferences.userId.first()
        if (userId != null) {
            // Memanggil query delete langsung dari DAO
            mangaDao.deleteManga(mangaId, userId)
        }
    }

    suspend fun updateManga(
        mangaId: Int,
        userId: Int,
        status: String,
        rating: Int?,
        volumeOwned: Int
    ) {
        mangaDao.updateManga(mangaId, userId, status, rating, volumeOwned)
    }
}