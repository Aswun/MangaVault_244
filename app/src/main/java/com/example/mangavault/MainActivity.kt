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

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as MangaVaultApplication).container
        val factory = ViewModelFactory(appContainer)

        setContent {
            val mainViewModel: MainViewModel = viewModel(factory = factory)
            val isDarkTheme by mainViewModel.isDarkMode.collectAsState()
            val startDestination by mainViewModel.startDestination.collectAsState()

            MangaVaultTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()

                // ViewModel
                val loginViewModel: LoginViewModel = viewModel(factory = factory)
                val libraryViewModel: LibraryViewModel = viewModel(factory = factory)
                val searchViewModel: SearchViewModel = viewModel(factory = factory)
                val settingsViewModel: SettingsViewModel = viewModel(factory = factory)

                if (startDestination != null) {
                    // Gunakan Scaffold untuk menempatkan BottomNavigationBar
                    Scaffold(
                        bottomBar = {
                            // Pasang BottomNavigationBar disini
                            BottomNavigationBar(navController = navController)
                        }
                    ) { innerPadding ->
                        // Terapkan padding dari Scaffold ke AppNavHost
                        AppNavHost(
                            navController = navController,
                            startDestination = startDestination!!,
                            mainViewModel = mainViewModel,
                            loginViewModel = loginViewModel,
                            libraryViewModel = libraryViewModel,
                            searchViewModel = searchViewModel,
                            settingsViewModel = settingsViewModel,
                            modifier = Modifier.padding(innerPadding) // Penting agar konten tidak tertutup bar
                        )
                    }
                }
            }
        }
    }
}