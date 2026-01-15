package com.example.mangavault.di

import android.content.Context
import com.example.mangavault.data.local.MangaVaultDatabase
import com.example.mangavault.data.repository.AuthRepository
import com.example.mangavault.data.repository.LibraryRepository
import com.example.mangavault.datastore.SessionPreferences

class AppContainer(context: Context) {

    /* DATABASE */
    private val database = MangaVaultDatabase.getInstance(context)

    /* DATASTORE */
    private val sessionPreferences = SessionPreferences(context)

    /* REPOSITORIES */
    val authRepository = AuthRepository(
        userDao = database.userDao(),
        sessionPreferences = sessionPreferences
    )

    val libraryRepository = LibraryRepository(
        mangaDao = database.mangaDao(),
        sessionPreferences = sessionPreferences
    )

    /* PROVIDERS */
    fun provideSessionPreferences() = sessionPreferences

    val searchRepository = SearchRepository()

}
