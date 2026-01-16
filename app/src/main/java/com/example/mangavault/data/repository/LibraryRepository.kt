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

class LibraryRepository(
    private val mangaDao: MangaDao,
    private val sessionPreferences: SessionPreferences
) {
    // REVISI: Menggunakan flatMapLatest agar Flow bereaksi terhadap perubahan userId (Login/Logout)
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
        // Pastikan userId valid sebelum update (untuk keamanan)
        val currentUserId = sessionPreferences.userId.first()
        if (currentUserId != null && currentUserId == userId) {
            mangaDao.updateManga(mangaId, userId, status, rating, volumeOwned)
        }
    }
}