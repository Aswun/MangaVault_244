package com.example.mangavault.data.repository

import com.example.mangavault.data.local.dao.UserDao
import com.example.mangavault.datastore.SessionPreferences
import com.example.mangavault.util.PasswordHasher

/**
 * Repository yang menangani logika Autentikasi Pengguna.
 * Menghubungkan database lokal (UserDao) dan penyimpanan sesi (SessionPreferences).
 */
class AuthRepository(
    private val userDao: UserDao,
    private val sessionPreferences: SessionPreferences
) {

    /**
     * Melakukan proses login lokal.
     * Memverifikasi username dan mencocokkan hash password.
     * Jika valid, sesi pengguna akan disimpan.
     *
     * @param username Nama pengguna inputan user.
     * @param password Password inputan user (plain text).
     * @return Result.success jika login berhasil, Result.failure jika gagal.
     */
    suspend fun login(
        username: String,
        password: String
    ): Result<Unit> {

        val user = userDao.getUserByUsername(username)
            ?: return Result.failure(
                IllegalArgumentException("Username atau password salah")
            )

        val isValid = PasswordHasher.verify(
            inputPassword = password,
            storedHash = user.passwordHash
        )

        if (!isValid) {
            return Result.failure(
                IllegalArgumentException("Username atau password salah")
            )
        }

        sessionPreferences.saveSession(user.userId)
        return Result.success(Unit)
    }

    /**
     * Menghapus sesi pengguna saat ini (Logout).
     */
    suspend fun logout() {
        sessionPreferences.clearSession()
    }
}