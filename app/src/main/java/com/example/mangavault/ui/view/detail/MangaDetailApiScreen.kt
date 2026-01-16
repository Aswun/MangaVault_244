package com.example.mangavault.ui.view.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mangavault.ui.viewmodel.search.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailApiScreen(
    mangaId: Int,
    viewModel: SearchViewModel,
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    // Mencari manga berdasarkan ID dari list hasil search
    // Pastikan menggunakan it.malId (camelCase sesuai DTO)
    val manga = viewModel.results.collectAsState().value.find { it.malId == mangaId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manga Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                manga?.let {
                    viewModel.saveToLibrary(it)
                    onSaveSuccess()
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Save to Library")
            }
        }
    ) { padding ->
        if (manga == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Manga data not found. Please search again.")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Gambar Cover (Gunakan largeImageUrl jika ada, fallback ke imageUrl)
                AsyncImage(
                    model = manga.images?.jpg?.largeImageUrl ?: manga.images?.jpg?.imageUrl,
                    contentDescription = manga.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = manga.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        MangaInfoBadge(label = "Score", value = manga.score?.toString() ?: "N/A")
                        MangaInfoBadge(label = "Type", value = manga.type ?: "?")
                        MangaInfoBadge(label = "Status", value = manga.status ?: "?")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Synopsis",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = manga.synopsis ?: "No synopsis available.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun MangaInfoBadge(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}