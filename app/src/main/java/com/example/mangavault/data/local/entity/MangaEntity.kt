package com.example.mangavault.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

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

    /**
     * ID manga dari Jikan API
     * Bagian dari Composite Primary Key
     */
    val mangaId: Int,

    /**
     * ID user pemilik manga
     * Bagian dari Composite Primary Key
     */
    val userId: Int,

    val title: String,

    val imageUrl: String?,

    /**
     * Status baca:
     * "Reading", "Completed", "Plan to Read"
     */
    val status: String,

    /**
     * Jumlah volume yang dimiliki user
     */
    val volumeOwned: Int = 0,

    /**
     * Rating user (1–10)
     * Nullable jika user belum memberi rating
     */
    val rating: Int?
)
