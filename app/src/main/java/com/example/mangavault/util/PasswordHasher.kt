package com.example.mangavault.util

import java.security.MessageDigest

/**
 * Object utilitas untuk menangani keamanan password.
 * Menggunakan algoritma SHA-256 untuk hashing password sebelum disimpan ke database.
 * Sesuai dengan requirement REQ-SEC-02 (Password hashing).
 */
object PasswordHasher {

    /**
     * Mengubah teks password biasa menjadi string Hexadecimal hasil hash SHA-256.
     *
     * @param password Password dalam bentuk plain text.
     * @return String hash SHA-256.
     */
    fun hash(password: String): String {
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(password.toByteArray())

        return bytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * Memverifikasi apakah password yang dimasukkan cocok dengan hash yang tersimpan.
     *
     * @param inputPassword Password input dari user (plain text).
     * @param storedHash Hash password yang tersimpan di database.
     * @return True jika cocok, False jika tidak.
     */
    fun verify(
        inputPassword: String,
        storedHash: String
    ): Boolean {
        return hash(inputPassword) == storedHash
    }
}