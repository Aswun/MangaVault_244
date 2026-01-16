package com.example.mangavault.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters // Import ini
import com.example.mangavault.data.local.dao.MangaDao
import com.example.mangavault.data.local.dao.UserDao
import com.example.mangavault.data.local.entity.MangaEntity
import com.example.mangavault.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, MangaEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class) // Tambahkan baris ini
abstract class MangaVaultDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun mangaDao(): MangaDao

    companion object {
        @Volatile
        private var Instance: MangaVaultDatabase? = null

        fun getDatabase(context: Context): MangaVaultDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    MangaVaultDatabase::class.java,
                    "mangavault_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}