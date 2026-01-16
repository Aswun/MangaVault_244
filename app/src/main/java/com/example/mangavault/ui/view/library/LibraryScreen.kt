package com.example.mangavault.ui.view.library

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mangavault.data.local.entity.MangaEntity
import com.example.mangavault.ui.view.components.ConfirmDeleteDialog
import com.example.mangavault.ui.view.components.EditMangaDialog
import com.example.mangavault.ui.view.components.LoadingIndicator
import com.example.mangavault.ui.view.components.MangaItem
import com.example.mangavault.ui.viewmodel.library.LibraryState
import com.example.mangavault.ui.viewmodel.library.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    onNavigateToDetail: (Int) -> Unit
) {
    // 1. Ambil UI State dari ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // 2. State Lokal untuk Dialog (Pengganti state di ViewModel)
    var mangaToDelete by remember { mutableStateOf<MangaEntity?>(null) }
    var mangaToEdit by remember { mutableStateOf<MangaEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Library") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when (val state = uiState) {
                is LibraryState.Loading -> {
                    LoadingIndicator()
                }
                is LibraryState.Empty -> {
                    Text(
                        text = "Library is empty.\nStart searching to add manga!",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is LibraryState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is LibraryState.Success -> {
                    // Tampilkan List Manga
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(150.dp),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.mangaList) { manga ->
                            MangaItem(
                                manga = manga, // FIX: Pass object manga utuh
                                onClick = {
                                    // Klik item untuk ke detail atau edit (tergantung preferensi)
                                    // Disini kita set agar klik membuka detail, tapi bisa juga edit
                                    onNavigateToDetail(manga.mangaId)
                                },
                                onDelete = {
                                    // Trigger dialog delete lokal
                                    mangaToDelete = manga
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // --- LOGIKA DIALOG (Dikelola di UI, bukan ViewModel) ---

    // 1. Dialog Edit (Opsional, jika ingin edit lewat klik lama atau tombol lain, bisa disesuaikan)
    // Jika Anda ingin fitur edit muncul saat klik item (bukan ke detail), ubah onClick di atas.
    // Atau tambahkan tombol edit di MangaItem.
    // Jika mangaToEdit != null, tampilkan dialog:
    mangaToEdit?.let { manga ->
        EditMangaDialog(
            manga = manga,
            onDismiss = { mangaToEdit = null },
            onSave = { status, rating, volumeOwned ->
                // Update object manga dengan value baru
                val updatedManga = manga.copy(
                    status = status,
                    rating = rating,
                    volumeOwned = volumeOwned
                )
                viewModel.updateManga(updatedManga)
                mangaToEdit = null
            }
        )
    }

    // 2. Dialog Delete
    mangaToDelete?.let { manga ->
        ConfirmDeleteDialog(
            title = manga.title,
            onConfirm = {
                viewModel.deleteManga(manga.mangaId)
                mangaToDelete = null
            },
            onDismiss = {
                mangaToDelete = null
            }
        )
    }
}