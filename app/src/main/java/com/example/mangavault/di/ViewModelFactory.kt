package com.example.mangavault.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mangavault.ui.viewmodel.MainViewModel
import com.example.mangavault.ui.viewmodel.auth.LoginViewModel
import com.example.mangavault.ui.viewmodel.library.LibraryViewModel
import com.example.mangavault.ui.viewmodel.search.SearchViewModel
import com.example.mangavault.ui.viewmodel.settings.SettingsViewModel

class ViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(container.provideSessionPreferences()) as T

            modelClass.isAssignableFrom(LoginViewModel::class.java) ->
                LoginViewModel(container.authRepository) as T

            modelClass.isAssignableFrom(LibraryViewModel::class.java) ->
                LibraryViewModel(container.libraryRepository) as T

            modelClass.isAssignableFrom(SearchViewModel::class.java) ->
                SearchViewModel(
                    container.searchRepository,
                    container.libraryRepository
                ) as T

            // --- TAMBAHKAN BAGIAN INI ---
            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(container.provideSessionPreferences()) as T

            else -> throw IllegalArgumentException(
                "Unknown ViewModel class: ${modelClass.name}"
            )
        }
    }
}