package com.example.mangavault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.mangavault.di.ViewModelFactory
import com.example.mangavault.ui.navigation.AppNavHost
import com.example.mangavault.ui.theme.MangaVaultTheme
import com.example.mangavault.ui.view.components.BottomNavigationBar // Import komponen baru
import com.example.mangavault.ui.viewmodel.MainViewModel
import com.example.mangavault.ui.viewmodel.auth.LoginViewModel
import com.example.mangavault.ui.viewmodel.library.LibraryViewModel
import com.example.mangavault.ui.viewmodel.search.SearchViewModel
import com.example.mangavault.ui.viewmodel.settings.SettingsViewModel

/**
 * Entry point utama aplikasi.
 * Bertanggung jawab untuk:
 * 1. Menginisialisasi Tema (Dark/Light Mode).
 * 2. Menyiapkan Navigasi (NavController).
 * 3. Menampilkan Splash Screen (Loading) sebelum routing awal ditentukan.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mengambil container dependensi dari Application class
        val appContainer = (application as MangaVaultApplication).container
        val factory = ViewModelFactory(appContainer)

        setContent {
            val mainViewModel: MainViewModel = viewModel(factory = factory)
            val isDarkTheme by mainViewModel.isDarkMode.collectAsState()
            val startDestination by mainViewModel.startDestination.collectAsState()

            MangaVaultTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()

                val loginViewModel: LoginViewModel = viewModel(factory = factory)
                val libraryViewModel: LibraryViewModel = viewModel(factory = factory)
                val searchViewModel: SearchViewModel = viewModel(factory = factory)
                val settingsViewModel: SettingsViewModel = viewModel(factory = factory)

                if (startDestination != null) {
                    Scaffold(
                        bottomBar = {
                            // Bottom Bar ditampilkan di semua halaman utama
                            BottomNavigationBar(navController = navController)
                        }
                    ) { innerPadding ->
                        AppNavHost(
                            navController = navController,
                            startDestination = startDestination!!,
                            mainViewModel = mainViewModel,
                            loginViewModel = loginViewModel,
                            libraryViewModel = libraryViewModel,
                            searchViewModel = searchViewModel,
                            settingsViewModel = settingsViewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}