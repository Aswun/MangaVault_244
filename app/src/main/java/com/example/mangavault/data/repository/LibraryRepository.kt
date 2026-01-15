package com.example.mangavault.data.repository

import com.example.mangavault.data.local.dao.MangaDao
import com.example.mangavault.data.local.entity.MangaEntity
import com.example.mangavault.datastore.SessionPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class LibraryRepository(
    private val mangaDao: MangaDao,
    private val sessionPreferences: SessionPreferences
) {

    suspend fun getUserManga(): Flow<List<MangaEntity>> {
        val userId = sessionPreferences.userId.first()
            ?: error("User not logged in")
        return mangaDao.getMangaByUser(userId)
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
            ?: error("User not logged in")

        mangaDao.upsertManga(
            MangaEntity(
                mangaId = mangaId,
                userId = userId,
                title = title,
                imageUrl = imageUrl,
                status = status,
                volumeOwned = volumeOwned,
                rating = rating
            )
        )
    }

    suspend fun deleteManga(mangaId: Int) {
        val userId = sessionPreferences.userId.first()
            ?: error("User not logged in")

        mangaDao.deleteManga(mangaId, userId)
    }

    suspend fun updateManga(
        mangaId: Int,
        status: String,
        rating: Int?,
        volumeOwned: Int
    ) {
        val userId = sessionPreferences.userId.first()
            ?: error("User not logged in")

        mangaDao.updateManga(
            mangaId = mangaId,
            userId = userId,
            status = status,
            rating = rating,
            volumeOwned = volumeOwned
        )
    }
}
