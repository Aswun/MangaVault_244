package com.example.mangavault.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mangavault.ui.viewmodel.MainViewModel
import com.example.mangavault.ui.viewmodel.auth.LoginViewModel
import com.example.mangavault.ui.viewmodel.library.LibraryViewModel

class ViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {

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

            else -> throw IllegalArgumentException(
                "Unknown ViewModel class: ${modelClass.name}"
            )
        }
    }
}
