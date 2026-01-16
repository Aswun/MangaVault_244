package com.example.mangavault.data.local.dao

import androidx.room.*
import com.example.mangavault.data.local.entity.MangaEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) untuk tabel Manga.
 * Menyediakan method untuk operasi CRUD dan Query data.
 * Semua query Read dibatasi berdasarkan [userId] untuk isolasi data antar pengguna.
 */
@Dao
interface MangaDao {

    // Mengambil semua manga user diurutkan berdasarkan Judul (A-Z)
    @Query("SELECT * FROM manga WHERE userId = :userId ORDER BY title ASC")
    fun getMangaByTitle(userId: Int): Flow<List<MangaEntity>>

    // Mengambil semua manga user diurutkan berdasarkan Status Baca
    @Query("SELECT * FROM manga WHERE userId = :userId ORDER BY status ASC")
    fun getMangaByStatus(userId: Int): Flow<List<MangaEntity>>

    // Mengambil semua manga user diurutkan berdasarkan Rating (Tertinggi ke Terendah)
    @Query("SELECT * FROM manga WHERE userId = :userId ORDER BY rating DESC")
    fun getMangaByRating(userId: Int): Flow<List<MangaEntity>>

    // Menyimpan atau Mengupdate data manga (Upsert)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertManga(manga: MangaEntity)

    // Menghapus data manga spesifik milik user tertentu
    @Query("DELETE FROM manga WHERE mangaId = :mangaId AND userId = :userId")
    suspend fun deleteManga(mangaId: Int, userId: Int)

    // Mengupdate metadata manga (Status, Rating, Volume) tanpa mengubah data lain
    @Query("UPDATE manga SET status = :status, rating = :rating, volumeOwned = :volumeOwned WHERE mangaId = :mangaId AND userId = :userId")
    suspend fun updateManga(mangaId: Int, userId: Int, status: String, rating: Int?, volumeOwned: Int)
}