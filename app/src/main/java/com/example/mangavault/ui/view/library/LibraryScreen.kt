package com.example.mangavault.ui.view.library

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mangavault.ui.view.components.ConfirmDeleteDialog
import com.example.mangavault.ui.view.components.EditMangaDialog
import com.example.mangavault.ui.view.components.MangaItem
import com.example.mangavault.ui.viewmodel.library.LibraryViewModel

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    onLogout: () -> Unit
) {
    val mangaList by viewModel.mangaList.collectAsState()
    val selectedManga by viewModel.selectedManga.collectAsState()
    val mangaToDelete by viewModel.mangaToDelete.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        /* HEADER */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "My Manga Library",
                style = MaterialTheme.typography.headlineMedium
            )

            TextButton(onClick = onLogout) {
                Text("Logout")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        /* LIST */
        if (mangaList.isEmpty()) {
            Text("No manga in your library")
        } else {
            LazyColumn {
                items(mangaList) { manga ->
                    MangaItem(
                        manga = manga,
                        onClick = {
                            viewModel.selectManga(manga)
                        },
                        onDelete = {
                            viewModel.requestDelete(manga)
                        }
                    )
                }
            }
        }
    }

    /* EDIT DIALOG */
    selectedManga?.let { manga ->
        EditMangaDialog(
            manga = manga,
            onDismiss = { viewModel.clearSelection() },
            onSave = { status, rating, volumeOwned ->
                viewModel.updateManga(
                    mangaId = manga.mangaId,
                    status = status,
                    rating = rating,
                    volumeOwned = volumeOwned
                )
            }
        )
    }

    /* CONFIRM DELETE DIALOG */
    mangaToDelete?.let { manga ->
        ConfirmDeleteDialog(
            title = manga.title,
            onConfirm = {
                viewModel.confirmDelete()
            },
            onDismiss = {
                viewModel.cancelDelete()
            }
        )
    }
}
