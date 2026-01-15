package com.example.mangavault

import android.app.Application
import com.example.mangavault.di.AppContainer

class MangaVaultApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
