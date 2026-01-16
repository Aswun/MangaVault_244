package com.example.mangavault.di

import android.content.Context
import com.example.mangavault.data.local.database.MangaVaultDatabase
import com.example.mangavault.data.remote.RetrofitClient
import com.example.mangavault.data.repository.AuthRepository
import com.example.mangavault.data.repository.LibraryRepository
import com.example.mangavault.data.repository.SearchRepository
import com.example.mangavault.datastore.SessionPreferences

class AppContainer(context: Context) {
    // Pastikan ini 'val' (public) agar bisa diakses Factory
    val sessionPreferences = SessionPreferences(context)

    private val database = MangaVaultDatabase.getDatabase(context)
    val authRepository = AuthRepository(database.userDao(), sessionPreferences)
    val libraryRepository = LibraryRepository(database.mangaDao(), sessionPreferences)
    val searchRepository = SearchRepository()
}