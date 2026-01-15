package com.example.mangavault.ui.view.about

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {
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
    }
}
