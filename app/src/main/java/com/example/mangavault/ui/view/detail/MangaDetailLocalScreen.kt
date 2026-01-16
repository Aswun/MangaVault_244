package com.example.mangavault.ui.view.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mangavault.ui.view.components.ConfirmDeleteDialog
import com.example.mangavault.ui.view.components.EditMangaDialog
import com.example.mangavault.ui.viewmodel.library.LibraryState
import com.example.mangavault.ui.viewmodel.library.LibraryViewModel

/**
 * Layar Detail Manga Lokal (Library).
 * Menampilkan informasi lengkap mengenai manga yang telah disimpan di database lokal.
 * Fitur:
 * - Melihat detail (Cover, Judul, Status, Rating, Volume).
 * - Mengedit data manga (Status, Rating, Volume) via Dialog.
 * - Menghapus manga dari library via Dialog Konfirmasi.
 *
 * @param mangaId ID unik manga yang akan ditampilkan detailnya.
 * @param viewModel ViewModel Library untuk operasi update dan delete.
 * @param onBack Callback navigasi untuk kembali ke layar sebelumnya.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailLocalScreen(
    mangaId: Int,
    viewModel: LibraryViewModel,
    onBack: () -> Unit
) {
    // Mengambil state UI terkini dari LibraryViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Mencari objek manga spesifik berdasarkan ID dari daftar manga di state Success
    val manga = (uiState as? LibraryState.Success)?.mangaList?.find { it.mangaId == mangaId }

    // State lokal untuk mengontrol visibilitas dialog
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = manga?.title ?: "Detail Manga",
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (manga != null) {
                        // Tombol Edit: Membuka dialog edit
                        IconButton(onClick = { showEditDialog = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        // Tombol Delete: Membuka dialog konfirmasi hapus
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (manga != null) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Menampilkan Gambar Cover Manga
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(manga.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = manga.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(350.dp)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Menampilkan Judul Manga
                Text(
                    text = manga.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Kartu Informasi Detail (Status, Volume, Rating)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Status: ${manga.status}", style = MaterialTheme.typography.bodyLarge)
                        Text("Volume Owned: ${manga.volumeOwned}", style = MaterialTheme.typography.bodyLarge)
                        Text("Rating: ${manga.rating ?: "-"}/10", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            // --- DIALOG EDIT ---
            // Menangani input user untuk memperbarui data manga
            if (showEditDialog) {
                EditMangaDialog(
                    manga = manga,
                    onDismiss = { showEditDialog = false },
                    onSave = { status, rating, volume ->
                        viewModel.updateManga(
                            manga.copy(
                                status = status,
                                rating = rating,
                                volumeOwned = volume
                            )
                        )
                        showEditDialog = false
                    }
                )
            }

            // --- DIALOG DELETE ---
            // Menangani konfirmasi penghapusan data
            if (showDeleteDialog) {
                ConfirmDeleteDialog(
                    title = manga.title,
                    onConfirm = {
                        // 1. Eksekusi hapus di ViewModel
                        viewModel.deleteManga(manga.mangaId)
                        // 2. Tutup dialog
                        showDeleteDialog = false
                        // 3. Kembali ke layar Library karena item sudah tidak ada
                        onBack()
                    },
                    onDismiss = {
                        showDeleteDialog = false
                    }
                )
            }

        } else {
            // Tampilan Loading atau Fallback jika data manga tidak ditemukan/sedang dimuat
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}