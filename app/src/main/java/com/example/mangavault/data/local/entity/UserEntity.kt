package com.example.mangavault.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

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

    /**
     * Password TIDAK disimpan plain text
     * Disimpan dalam bentuk hash (REQ-SEC-02)
     */
    val passwordHash: String
)
