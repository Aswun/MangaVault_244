package com.example.mangavault.ui.view.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mangavault.ui.view.components.EditMangaDialog
import com.example.mangavault.ui.viewmodel.library.LibraryState
import com.example.mangavault.ui.viewmodel.library.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailLocalScreen(
    mangaId: Int,
    viewModel: LibraryViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Ambil data manga dari state Success
    val manga = (uiState as? LibraryState.Success)?.mangaList?.find { it.mangaId == mangaId }
    var showEditDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Collection") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        manga?.let {
                            viewModel.deleteManga(it.mangaId)
                            onBack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showEditDialog = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Progress")
            }
        }
    ) { padding ->
        if (manga == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Manga not found in library")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = manga.imageUrl,
                    contentDescription = manga.title,
                    modifier = Modifier.fillMaxWidth().height(350.dp),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = manga.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(24.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Reading Progress", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                DetailItem(label = "Status", value = manga.status)
                                DetailItem(label = "Volumes", value = "${manga.volumeOwned}")
                                DetailItem(label = "My Rating", value = if (manga.rating != null && manga.rating > 0) "${manga.rating}/10" else "-")
                            }
                        }
                    }
                }
            }

            if (showEditDialog) {
                EditMangaDialog(
                    manga = manga,
                    onDismiss = { showEditDialog = false },
                    onSave = { status, rating, volumeOwned ->
                        viewModel.updateManga(
                            manga.copy(status = status, rating = rating, volumeOwned = volumeOwned)
                        )
                        showEditDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}