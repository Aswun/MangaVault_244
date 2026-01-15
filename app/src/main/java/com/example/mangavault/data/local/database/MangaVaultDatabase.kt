package com.example.mangavault.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mangavault.data.local.dao.MangaDao
import com.example.mangavault.data.local.dao.UserDao
import com.example.mangavault.data.local.entity.MangaEntity
import com.example.mangavault.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        MangaEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class MangaVaultDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun mangaDao(): MangaDao
}
