package com.example.mangavault

import android.app.Application
import com.example.mangavault.di.AppContainer

/**
 * Class Application utama untuk inisialisasi komponen global aplikasi.
 * Bertanggung jawab untuk menyiapkan Dependency Injection container.
 */
class MangaVaultApplication : Application() {

    /**
     * Container utama untuk Dependency Injection manual.
     * Dapat diakses dari seluruh aplikasi.
     */
    lateinit var container: AppContainer
        private set

    /**
     * Dipanggil saat aplikasi pertama kali dibuat.
     * Menginisialisasi AppContainer.
     */
    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}