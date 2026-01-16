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

/**
 * Layar Detail Manga (Versi API/Online).
 * Menampilkan informasi detail manga yang diambil dari hasil pencarian API.
 * Menyediakan opsi untuk menyimpan manga ke Library lokal.
 *
 * @param mangaId ID Manga (MAL ID) yang dipilih.
 * @param viewModel ViewModel untuk mengakses data hasil pencarian sebelumnya.
 * @param onBack Callback navigasi kembali.
 * @param onSaveSuccess Callback saat manga berhasil disimpan.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailApiScreen(
    mangaId: Int,
    viewModel: SearchViewModel,
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    // Mengambil data detail manga dari cache hasil pencarian di ViewModel
    val manga = viewModel.results.collectAsState().value.find { it.malId == mangaId }
    val context = LocalContext.current

    // State untuk kontrol dialog "Add to Library"
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
                // Header Gambar
                AsyncImage(
                    model = manga.images?.jpg?.largeImageUrl ?: manga.images?.jpg?.imageUrl,
                    contentDescription = manga.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    // Judul & Info Dasar
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

    // Dialog Input Metadata sebelum Simpan
    if (showAddDialog && manga != null) {
        val tempEntity = MangaEntity(
            mangaId = manga.malId,
            userId = 0,
            title = manga.title,
            imageUrl = manga.images?.jpg?.imageUrl,
            status = "Plan to Read",
            volumeOwned = 0,
            rating = null
        )

        EditMangaDialog(
            manga = tempEntity,
            onDismiss = { showAddDialog = false },
            onSave = { status, rating, volume ->
                viewModel.saveToLibrary(
                    manga = manga,
                    status = status,
                    rating = rating,
                    volumeOwned = volume
                )
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