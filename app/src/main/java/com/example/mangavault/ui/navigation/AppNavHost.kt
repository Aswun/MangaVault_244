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
            LibraryScreen(
                viewModel = libraryViewModel,
                onNavigateToDetail = { mangaId ->
                    navController.navigate(NavRoute.DetailLocal.createRoute(mangaId))
                }
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

        composable(NavRoute.Setting.route) {
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateToAbout = {
                    navController.navigate(NavRoute.About.route)
                },
                onLogoutSuccess = {
                    // PERBAIKAN: Reset state LoginViewModel agar tidak auto-login
                    loginViewModel.resetState()

                    navController.navigate(NavRoute.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onLoginClick = {
                    // PERBAIKAN: Reset state juga saat tombol login manual ditekan
                    loginViewModel.resetState()

                    navController.navigate(NavRoute.Login.route)
                }
            )
        }

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