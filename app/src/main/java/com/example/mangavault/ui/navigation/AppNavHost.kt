package com.example.mangavault.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
// Pastikan package ini sesuai dengan file SettingsScreen.kt kamu.
// Jika di file SettingsScreen package-nya "ui.view.setting", ubah import di bawah ini.
import com.example.mangavault.ui.view.settings.SettingScreen
import com.example.mangavault.ui.viewmodel.MainViewModel
import com.example.mangavault.ui.viewmodel.auth.LoginViewModel
import com.example.mangavault.ui.viewmodel.library.LibraryViewModel
import com.example.mangavault.ui.viewmodel.search.SearchViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel,
    libraryViewModel: LibraryViewModel,
    searchViewModel: SearchViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        // 1. Rute Login
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

        // 2. Rute Library (Offline)
        composable(NavRoute.Library.route) {
            LibraryScreen(
                viewModel = libraryViewModel,
                onLogout = {
                    navController.navigate(NavRoute.Login.route) {
                        popUpTo(NavRoute.Library.route) { inclusive = true }
                    }
                }
            )
        }

        // 3. Rute Search (Online)
        composable(NavRoute.Search.route) {
            SearchScreen(
                viewModel = searchViewModel,
                navController = navController // Passing navController untuk navigasi ke detail
            )
        }

        // 4. Rute About
        composable(NavRoute.About.route) {
            AboutScreen()
        }

        // 5. Rute Setting (Dengan Fitur Ganti Tema)
        composable(NavRoute.Setting.route) {
            // Mengambil state tema dari MainViewModel
            val isDarkTheme by mainViewModel.isDarkMode.collectAsState()

            SettingScreen(
                isDarkTheme = isDarkTheme,
                onThemeChanged = { newThemeValue ->
                    mainViewModel.toggleTheme(newThemeValue)
                },
                onLogout = {
                    libraryViewModel.logout() // Pastikan ada fungsi logout di LibraryViewModel
                    navController.navigate(NavRoute.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        // 6. Rute Detail API (Dari Search ke Detail)
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
                    // Opsional: Bisa diarahkan ke Library jika diinginkan
                }
            )
        }

        // 7. Rute Detail Local (Dari Library ke Detail)
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