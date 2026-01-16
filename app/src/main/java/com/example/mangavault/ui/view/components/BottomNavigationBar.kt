package com.example.mangavault.ui.view.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mangavault.ui.navigation.NavRoute

/**
 * Data class untuk mendefinisikan item menu pada navigasi bawah.
 */
data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

/**
 * Komponen Bottom Navigation Bar aplikasi.
 * Menangani navigasi antara Library, Search, dan Settings.
 * Otomatis menyembunyikan diri jika berada di route yang tidak terdaftar.
 *
 * @param navController NavController untuk menangani perpindahan layar.
 */
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Library", Icons.Default.Collections, NavRoute.Library.route),
        BottomNavItem("Search", Icons.Default.Search, NavRoute.Search.route),
        BottomNavItem("Settings", Icons.Default.Settings, NavRoute.Setting.route)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = items.any { it.route == currentRoute }

    if (showBottomBar) {
        NavigationBar {
            items.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(item.label) },
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(NavRoute.Library.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}