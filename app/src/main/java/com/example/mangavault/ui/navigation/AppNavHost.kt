package com.example.mangavault.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mangavault.ui.view.about.AboutScreen
import com.example.mangavault.ui.view.auth.LoginScreen
import com.example.mangavault.ui.view.detail.MangaDetailApiScreen
import com.example.mangavault.ui.view.detail.MangaDetailLocalScreen
import com.example.mangavault.ui.view.library.LibraryScreen
import com.example.mangavault.ui.view.search.SearchScreen
import com.example.mangavault.ui.view.settings.SettingsScreen // Pastikan Import ini Benar
import com.example.mangavault.ui.viewmodel.MainViewModel
import com.example.mangavault.ui.viewmodel.auth.LoginViewModel
import com.example.mangavault.ui.viewmodel.library.LibraryViewModel
import com.example.mangavault.ui.viewmodel.search.SearchViewModel
import com.example.mangavault.ui.viewmodel.settings.SettingsViewModel // Import ini

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel,
    libraryViewModel: LibraryViewModel,
    searchViewModel: SearchViewModel,
    settingsViewModel: SettingsViewModel, // Tambahkan Parameter Ini
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // ... (Route Login, Library, Search, About TETAP SAMA) ...

        composable(NavRoute.Login.route) {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(NavRoute.Library.route) {
                        popUpTo(NavRoute.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoute.Library.route) {
            // Hapus onLogout dari sini karena sudah pindah ke Settings
            LibraryScreen(
                viewModel = libraryViewModel,
                onLogout = { /* Tidak dipakai lagi disini */ }
                // Jika LibraryScreen masih minta onLogout, hapus parameter itu di file LibraryScreen.kt
                // Atau biarkan kosong sementara
            )
        }

        composable(NavRoute.Search.route) {
            SearchScreen(
                viewModel = searchViewModel,
                navController = navController
            )
        }

        composable(NavRoute.About.route) {
            AboutScreen()
        }

        // --- UPDATE ROUTE SETTINGS ---
        composable(NavRoute.Setting.route) {
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateToAbout = {
                    navController.navigate(NavRoute.About.route)
                },
                onLogoutSuccess = {
                    navController.navigate(NavRoute.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ... (Route Detail API & Local TETAP SAMA) ...

        composable(
            route = "detail_api/{mangaId}",
            arguments = listOf(navArgument("mangaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val mangaId = backStackEntry.arguments?.getInt("mangaId") ?: 0
            MangaDetailApiScreen(
                mangaId = mangaId,
                viewModel = searchViewModel,
                onBack = { navController.popBackStack() },
                onSaveSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "detail_local/{mangaId}",
            arguments = listOf(navArgument("mangaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val mangaId = backStackEntry.arguments?.getInt("mangaId") ?: 0
            MangaDetailLocalScreen(
                mangaId = mangaId,
                viewModel = libraryViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}