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

// Model data sederhana untuk item navigasi
data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavigationBar(navController: NavController) {
    // Daftar menu navigasi bawah
    val items = listOf(
        BottomNavItem("Library", Icons.Default.Collections, NavRoute.Library.route),
        BottomNavItem("Search", Icons.Default.Search, NavRoute.Search.route),
        BottomNavItem("Settings", Icons.Default.Settings, NavRoute.Setting.route)
    )

    // Mendapatkan route saat ini untuk menentukan item mana yang aktif
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Tampilkan NavigationBar hanya jika route saat ini termasuk dalam menu bawah
    // Ini mencegah bar muncul di halaman Login atau Detail yang tidak diinginkan
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
                            // Pop up ke start destination graph agar tidak menumpuk stack
                            popUpTo(NavRoute.Library.route) {
                                saveState = true
                            }
                            // Hindari duplikasi instance jika user mengetuk tombol yang sama
                            launchSingleTop = true
                            // Restore state saat kembali ke item tersebut
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}