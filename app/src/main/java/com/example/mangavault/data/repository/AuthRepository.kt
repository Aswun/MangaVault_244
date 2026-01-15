package com.example.mangavault.data.repository

import com.example.mangavault.data.local.dao.UserDao
import com.example.mangavault.datastore.SessionPreferences
import com.example.mangavault.util.PasswordHasher

class AuthRepository(
    private val userDao: UserDao,
    private val sessionPreferences: SessionPreferences
) {

    /**
     * Login lokal
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

    suspend fun logout() {
        sessionPreferences.clearSession()
    }
}
