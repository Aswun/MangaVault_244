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
import com.example.mangavault.ui.view.settings.SettingsScreen
import com.example.mangavault.ui.viewmodel.MainViewModel
import com.example.mangavault.ui.viewmodel.auth.LoginViewModel
import com.example.mangavault.ui.viewmodel.library.LibraryViewModel
import com.example.mangavault.ui.viewmodel.search.SearchViewModel
import com.example.mangavault.ui.viewmodel.settings.SettingsViewModel

/**
 * Komponen utama navigasi aplikasi (Navigation Graph).
 * Mengatur perpindahan antar layar (Screen) berdasarkan rute (Route).
 *
 * @param navController Controller navigasi dari Jetpack Navigation.
 * @param startDestination Rute awal yang ditampilkan saat aplikasi dibuka.
 * @param mainViewModel ViewModel global (jika diperlukan).
 * @param loginViewModel ViewModel untuk logika autentikasi.
 * @param libraryViewModel ViewModel untuk pengelolaan data koleksi manga lokal.
 * @param searchViewModel ViewModel untuk pencarian manga via API.
 * @param settingsViewModel ViewModel untuk pengaturan aplikasi.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel,
    libraryViewModel: LibraryViewModel,
    searchViewModel: SearchViewModel,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Halaman Login
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

        // Halaman Library (Koleksi Lokal)
        composable(NavRoute.Library.route) {
            LibraryScreen(
                viewModel = libraryViewModel,
                onNavigateToDetail = { mangaId ->
                    navController.navigate(NavRoute.DetailLocal.createRoute(mangaId))
                }
            )
        }

        // Halaman Pencarian (Online)
        composable(NavRoute.Search.route) {
            SearchScreen(
                viewModel = searchViewModel,
                navController = navController
            )
        }

        // Halaman About
        composable(NavRoute.About.route) {
            AboutScreen()
        }

        // Halaman Settings
        composable(NavRoute.Setting.route) {
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateToAbout = {
                    navController.navigate(NavRoute.About.route)
                },
                onLogoutSuccess = {
                    // Reset state saat logout agar tidak auto-login kembali
                    loginViewModel.resetState()

                    navController.navigate(NavRoute.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onLoginClick = {
                    // Reset state saat masuk halaman login secara manual
                    loginViewModel.resetState()

                    navController.navigate(NavRoute.Login.route)
                }
            )
        }

        // Halaman Detail Manga dari API (Preview sebelum save)
        composable(
            route = NavRoute.DetailApi.route,
            arguments = listOf(navArgument("mangaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val mangaId = backStackEntry.arguments?.getInt("mangaId") ?: 0
            MangaDetailApiScreen(
                mangaId = mangaId,
                viewModel = searchViewModel,
                onBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        // Halaman Detail Manga Lokal (View/Edit/Delete)
        composable(
            route = NavRoute.DetailLocal.route,
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