package com.example.mangavault.data.local.dao

import androidx.room.*
import com.example.mangavault.data.local.entity.MangaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MangaDao {
    // Default / Sort by Title
    @Query("SELECT * FROM manga WHERE userId = :userId ORDER BY title ASC")
    fun getMangaByTitle(userId: Int): Flow<List<MangaEntity>>

    // Sort by Status
    @Query("SELECT * FROM manga WHERE userId = :userId ORDER BY status ASC")
    fun getMangaByStatus(userId: Int): Flow<List<MangaEntity>>

    // Sort by Rating (Highest first)
    @Query("SELECT * FROM manga WHERE userId = :userId ORDER BY rating DESC")
    fun getMangaByRating(userId: Int): Flow<List<MangaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertManga(manga: MangaEntity)

    @Query("DELETE FROM manga WHERE mangaId = :mangaId AND userId = :userId")
    suspend fun deleteManga(mangaId: Int, userId: Int)

    @Query("UPDATE manga SET status = :status, rating = :rating, volumeOwned = :volumeOwned WHERE mangaId = :mangaId AND userId = :userId")
    suspend fun updateManga(mangaId: Int, userId: Int, status: String, rating: Int?, volumeOwned: Int)
}