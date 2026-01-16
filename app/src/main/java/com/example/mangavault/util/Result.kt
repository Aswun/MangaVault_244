package com.example.mangavault.util

/**
 * Sealed class generic untuk membungkus hasil operasi (misalnya request network atau database).
 * Memiliki 3 state: Success, Error, dan Loading.
 */
sealed class Result<out T> {

    /**
     * Menandakan operasi berhasil dan membawa data hasil.
     * @param data Data hasil operasi.
     */
    data class Success<out T>(val data: T) : Result<T>()

    /**
     * Menandakan operasi gagal.
     * @param message Pesan error yang user-friendly.
     * @param throwable Exception asli jika ada.
     */
    data class Error(val message: String, val throwable: Throwable? = null) : Result<Nothing>()

    /**
     * Menandakan operasi sedang berjalan.
     */
    object Loading : Result<Nothing>()
}