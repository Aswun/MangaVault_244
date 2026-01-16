package com.example.mangavault.ui.view.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mangavault.ui.navigation.NavRoute
import com.example.mangavault.ui.viewmodel.settings.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navController: NavController,
    // Callback tambahan jika Anda masih menggunakan versi lama AppNavHost
    onNavigateToAbout: () -> Unit = {},
    onLogoutSuccess: () -> Unit = {}
) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Section: Appearance
            SettingsSectionHeader(title = "Appearance")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DarkMode, contentDescription = null)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Dark Mode")
                }
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { viewModel.toggleDarkMode(it) }
                )
            }

            HorizontalDivider()

            // Section: Account
            SettingsSectionHeader(title = "Account")

            if (isLoggedIn) {
                // Tampilkan Profile & Logout jika user Login
                SettingsItem(
                    icon = Icons.Default.Person,
                    title = "Profile",
                    onClick = { /* Navigate to Profile if implemented */ }
                )

                SettingsItem(
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    title = "Logout",
                    textColor = MaterialTheme.colorScheme.error,
                    onClick = { viewModel.logout() }
                )
            } else {
                // Tampilkan Login jika user Logout
                SettingsItem(
                    icon = Icons.AutoMirrored.Filled.Login,
                    title = "Login",
                    textColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        // Arahkan ke halaman Login
                        navController.navigate(NavRoute.Login.route) {
                            // Perbaikan: Menggunakan NavRoute.Setting (sesuai file Anda)
                            // Hapus 'inclusive = false' karena itu default, untuk menghindari error akses private
                            popUpTo(NavRoute.Setting.route)
                        }
                    }
                )
            }

            HorizontalDivider()

            // Section: About
            SettingsSectionHeader(title = "About")

            SettingsItem(
                icon = Icons.Default.Info,
                title = "About MangaVault",
                onClick = { navController.navigate(NavRoute.About.route) }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Version 1.0.0",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}