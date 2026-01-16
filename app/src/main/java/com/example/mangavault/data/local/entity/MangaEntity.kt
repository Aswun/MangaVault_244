package com.example.mangavault.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Entitas database yang merepresentasikan Manga dalam koleksi pengguna.
 * Menggunakan Composite Primary Key (mangaId + userId) agar satu manga bisa dimiliki
 * oleh banyak pengguna secara terpisah.
 *
 * @property mangaId ID unik dari Jikan API.
 * @property userId ID pengguna pemilik manga ini (Foreign Key).
 * @property title Judul manga.
 * @property imageUrl URL gambar cover manga.
 * @property status Status baca ("Reading", "Completed", "Plan to Read").
 * @property volumeOwned Jumlah volume yang dimiliki (default 0).
 * @property rating Rating pribadi dari user (1-10), null jika belum ada.
 */
@Entity(
    tableName = "manga",
    primaryKeys = ["mangaId", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId"])
    ]
)
data class MangaEntity(
    val mangaId: Int,
    val userId: Int,
    val title: String,
    val imageUrl: String?,
    val status: String,
    val volumeOwned: Int = 0,
    val rating: Int?
)