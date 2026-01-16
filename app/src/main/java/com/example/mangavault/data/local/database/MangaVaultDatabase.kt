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

/**
 * Database utama Room untuk aplikasi.
 * Menyimpan tabel User dan Manga.
 */
@Database(
    entities = [UserEntity::class, MangaEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MangaVaultDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun mangaDao(): MangaDao

    /**
     * Callback untuk mengisi data awal (Seeding) saat database pertama kali dibuat.
     */
    private class MangaDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            // Mengisi data user dummy secara synchronous untuk memastikan data siap saat login
            val passAswin = PasswordHasher.hash("aswin123")
            val passArdhi = PasswordHasher.hash("ardhi123")

            db.execSQL(
                "INSERT INTO users (username, passwordHash) VALUES (?, ?)",
                arrayOf("aswin", passAswin)
            )

            db.execSQL(
                "INSERT INTO users (username, passwordHash) VALUES (?, ?)",
                arrayOf("ardhi", passArdhi)
            )
        }
    }

    companion object {
        @Volatile
        private var Instance: MangaVaultDatabase? = null

        /**
         * Mendapatkan instance singleton database.
         *
         * @param context Context aplikasi.
         * @return Instance MangaVaultDatabase.
         */
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