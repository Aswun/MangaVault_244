package com.example.mangavault.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mangavault.data.local.dao.MangaDao
import com.example.mangavault.data.local.dao.UserDao
import com.example.mangavault.data.local.entity.MangaEntity
import com.example.mangavault.data.local.entity.UserEntity
import com.example.mangavault.util.PasswordHasher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Database(
    entities = [UserEntity::class, MangaEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MangaVaultDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun mangaDao(): MangaDao

    // Callback untuk mengisi data awal (Seeding)
    private class MangaDatabaseCallback(
        private val scope: CoroutineScope // Scope tidak lagi digunakan untuk insert, tapi dibiarkan agar tidak merubah signature constructor
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            // REVISI PENTING: Menggunakan execSQL untuk insert Sync.
            // Ini mencegah Race Condition di mana login dilakukan sebelum data selesai dibuat.

            // 1. Hash Password
            val passAswin = PasswordHasher.hash("aswin123")
            val passArdhi = PasswordHasher.hash("ardhi123") // User ke-2

            // 2. Insert User 1: aswin
            db.execSQL(
                "INSERT INTO users (username, passwordHash) VALUES (?, ?)",
                arrayOf("aswin", passAswin)
            )

            // 3. Insert User 2: ardhi
            db.execSQL(
                "INSERT INTO users (username, passwordHash) VALUES (?, ?)",
                arrayOf("ardhi", passArdhi)
            )
        }
    }

    companion object {
        @Volatile
        private var Instance: MangaVaultDatabase? = null

        fun getDatabase(context: Context): MangaVaultDatabase {
            return Instance ?: synchronized(this) {
                val scope = CoroutineScope(Dispatchers.IO)

                Room.databaseBuilder(
                    context,
                    MangaVaultDatabase::class.java,
                    "mangavault_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(MangaDatabaseCallback(scope))
                    .build()
                    .also { Instance = it }
            }
        }
    }
}