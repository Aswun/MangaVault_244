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
import kotlinx.coroutines.launch

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
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Instance database belum tentu null, tapi kita gunakan safe call
            // Menggunakan Dispatchers.IO untuk operasi database di background
            Instance?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.userDao())
                }
            }
        }

        suspend fun populateDatabase(userDao: UserDao) {
            // Hapus data lama jika perlu (opsional untuk onCreate)
            // userDao.deleteAll()

            // Tambahkan User Manual: aswin / aswin123
            // PENTING: Password harus di-hash menggunakan PasswordHasher sebelum disimpan
            val user = UserEntity(
                username = "aswin",
                passwordHash = PasswordHasher.hash("aswin123")
            )
            userDao.insertUser(user)
        }
    }

    companion object {
        @Volatile
        private var Instance: MangaVaultDatabase? = null

        fun getDatabase(context: Context): MangaVaultDatabase {
            return Instance ?: synchronized(this) {
                // Membuat Scope sementara untuk callback database
                val scope = CoroutineScope(Dispatchers.IO)

                Room.databaseBuilder(
                    context,
                    MangaVaultDatabase::class.java,
                    "mangavault_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(MangaDatabaseCallback(scope)) // Tambahkan Callback di sini
                    .build()
                    .also { Instance = it }
            }
        }
    }
}