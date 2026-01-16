package com.example.mangavault.ui.view.library

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
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
import com.example.mangavault.ui.viewmodel.library.SortOption

/**
 * Layar Library (Koleksi Saya).
 * Menampilkan daftar manga yang telah disimpan pengguna secara lokal.
 * Mendukung fitur sorting, edit, dan hapus manga.
 *
 * @param viewModel ViewModel untuk mengambil data dari Room Database.
 * @param onNavigateToDetail Callback navigasi ke detail manga.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    onNavigateToDetail: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()

    // State lokal untuk manajemen dialog
    var mangaToDelete by remember { mutableStateOf<MangaEntity?>(null) }
    var mangaToEdit by remember { mutableStateOf<MangaEntity?>(null) }
    var showSortMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Library") },
                actions = {
                    // Menu Sorting
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(Icons.Default.List, contentDescription = "Sort Options")
                    }
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sort by Title") },
                            onClick = {
                                viewModel.updateSortOption(SortOption.TITLE)
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Sort by Status") },
                            onClick = {
                                viewModel.updateSortOption(SortOption.STATUS)
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Sort by Rating") },
                            onClick = {
                                viewModel.updateSortOption(SortOption.RATING)
                                showSortMenu = false
                            }
                        )
                    }
                }
            )
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
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(150.dp),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.mangaList) { manga ->
                            MangaItem(
                                manga = manga,
                                onClick = {
                                    onNavigateToDetail(manga.mangaId)
                                },
                                onDelete = {
                                    mangaToDelete = manga
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Menampilkan Dialog Edit jika ada manga yang dipilih untuk diedit
    mangaToEdit?.let { manga ->
        EditMangaDialog(
            manga = manga,
            onDismiss = { mangaToEdit = null },
            onSave = { status, rating, volumeOwned ->
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

    // Menampilkan Dialog Konfirmasi Hapus jika ada manga yang dipilih untuk dihapus
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