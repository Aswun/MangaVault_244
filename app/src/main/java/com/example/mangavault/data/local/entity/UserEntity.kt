package com.example.mangavault.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entitas database untuk menyimpan data Akun Pengguna.
 *
 * @property userId ID unik pengguna (Auto Generated).
 * @property username Nama pengguna (harus unik).
 * @property passwordHash Hash password pengguna (SHA-256). Password asli tidak disimpan demi keamanan.
 */
@Entity(
    tableName = "users",
    indices = [
        Index(value = ["username"], unique = true)
    ]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val username: String,
    val passwordHash: String
)