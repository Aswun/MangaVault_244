package com.example.mangavault.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mangavault.ui.view.auth.LoginScreen
import com.example.mangavault.ui.view.library.LibraryScreen
import com.example.mangavault.ui.viewmodel.auth.LoginViewModel
import com.example.mangavault.ui.viewmodel.library.LibraryViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    loginViewModel: LoginViewModel,
    libraryViewModel: LibraryViewModel,
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
                onLogout = {
                    navController.navigate(NavRoute.Login.route) {
                        popUpTo(NavRoute.Library.route) { inclusive = true }
                    }
                }
            )
        }
        composable(NavRoute.Search.route) {
            SearchScreen(searchViewModel)
        }

        composable(NavRoute.About.route) {
            AboutScreen()
        }

        composable(NavRoute.Setting.route) {
            SettingScreen(
                onLogout = {
                    libraryViewModel.logout()
                    navController.navigate(NavRoute.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }

    }
}
