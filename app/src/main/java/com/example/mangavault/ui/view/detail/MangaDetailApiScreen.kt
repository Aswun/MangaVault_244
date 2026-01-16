package com.example.mangavault.ui.view.detail

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mangavault.data.local.entity.MangaEntity
import com.example.mangavault.ui.view.components.EditMangaDialog
import com.example.mangavault.ui.viewmodel.search.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailApiScreen(
    mangaId: Int,
    viewModel: SearchViewModel,
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    // Mengambil data manga dari state results ViewModel berdasarkan ID
    val manga = viewModel.results.collectAsState().value.find { it.malId == mangaId }
    val context = LocalContext.current

    // State untuk mengontrol visibilitas Dialog
    var showAddDialog by remember { mutableStateOf(false) }

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
                // REVISI: Jangan langsung save. Buka dialog dulu jika data manga ada.
                if (manga != null) {
                    showAddDialog = true
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
                // Gambar Cover
                AsyncImage(
                    model = manga.images?.jpg?.largeImageUrl ?: manga.images?.jpg?.imageUrl,
                    contentDescription = manga.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    // Judul
                    Text(
                        text = manga.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Badge Informasi
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        MangaInfoBadge(label = "Score", value = manga.score?.toString() ?: "N/A")
                        MangaInfoBadge(label = "Type", value = manga.type ?: "?")
                        MangaInfoBadge(label = "Status", value = manga.status ?: "?")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sinopsis
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

    // LOGIKA DIALOG (REQ-SEA-07)
    if (showAddDialog && manga != null) {
        // Membuat objek sementara MangaEntity untuk mengisi nilai default di Dialog
        val tempEntity = MangaEntity(
            mangaId = manga.malId,
            userId = 0, // Dummy ID, akan diurus oleh Repository
            title = manga.title,
            imageUrl = manga.images?.jpg?.imageUrl,
            status = "Plan to Read", // Default Status
            volumeOwned = 0,
            rating = null
        )

        EditMangaDialog(
            manga = tempEntity,
            onDismiss = { showAddDialog = false },
            onSave = { status, rating, volume ->
                // Panggil ViewModel dengan data lengkap
                viewModel.saveToLibrary(
                    manga = manga,
                    status = status,
                    rating = rating,
                    volumeOwned = volume
                )

                // Tampilkan notifikasi dan tutup halaman detail
                Toast.makeText(context, "${manga.title} added to Library", Toast.LENGTH_SHORT).show()
                showAddDialog = false
                onSaveSuccess()
            }
        )
    }
}

@Composable
fun MangaInfoBadge(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}