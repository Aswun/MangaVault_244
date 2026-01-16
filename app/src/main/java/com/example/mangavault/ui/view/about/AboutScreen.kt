package com.example.mangavault.ui.view.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "MangaVault",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Version 1.0.0",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "MangaVault is a personal manga collection management application that allows users to search manga using Jikan API and manage their own library offline.",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Developed by:",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Aswin Lutfian",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "External Links",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tautan ke Repository (REQ-OTH-14)
        Text(
            text = "Project Repository (GitHub)",
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .clickable {
                    // Ganti URL dengan repository sebenarnya jika ada
                    uriHandler.openUri("https://github.com/aswinlutfian/MangaVault")
                }
                .padding(vertical = 4.dp)
        )

        // Tautan ke Jikan API (REQ-OTH-15)
        Text(
            text = "Jikan API Documentation",
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .clickable {
                    uriHandler.openUri("https://jikan.moe/")
                }
                .padding(vertical = 4.dp)
        )
    }
}