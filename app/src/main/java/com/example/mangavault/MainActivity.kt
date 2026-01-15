package com.example.mangavault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.mangavault.di.ViewModelFactory
import com.example.mangavault.ui.navigation.AppNavHost
import com.example.mangavault.ui.theme.MangaVaultTheme
import com.example.mangavault.ui.viewmodel.MainViewModel
import com.example.mangavault.ui.viewmodel.auth.LoginViewModel
import com.example.mangavault.ui.viewmodel.library.LibraryViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer =
            (application as MangaVaultApplication).container

        val factory = ViewModelFactory(appContainer)

        setContent {
            MangaVaultTheme {

                val navController = rememberNavController()

                val mainViewModel: MainViewModel =
                    viewModel(factory = factory)

                val loginViewModel: LoginViewModel =
                    viewModel(factory = factory)

                val libraryViewModel: LibraryViewModel =
                    viewModel(factory = factory)

                val startDestination by
                mainViewModel.startDestination.collectAsState()

                if (startDestination != null) {
                    AppNavHost(
                        navController = navController,
                        startDestination = startDestination!!,
                        loginViewModel = loginViewModel,
                        libraryViewModel = libraryViewModel
                    )
                }
            }
        }
    }
}
