package com.example.mangavault.ui.view.search

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mangavault.data.local.entity.MangaEntity
import com.example.mangavault.data.remote.dto.JikanMangaDto
import com.example.mangavault.ui.navigation.NavRoute
import com.example.mangavault.ui.view.components.EditMangaDialog
import com.example.mangavault.ui.view.components.LoadingIndicator
import com.example.mangavault.ui.viewmodel.search.SearchState
import com.example.mangavault.ui.viewmodel.search.SearchViewModel

/**
 * Layar Pencarian Manga.
 * Memungkinkan pengguna mencari manga via Jikan API dan menyimpannya ke koleksi lokal.
 *
 * @param viewModel ViewModel yang menangani logika pencarian dan penyimpanan data.
 * @param navController Controller navigasi untuk berpindah ke halaman detail.
 */
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    navController: NavController? = null
) {
    val state by viewModel.state.collectAsState()
    var query by remember { mutableStateOf("") }
    val context = LocalContext.current

    // State untuk mengontrol visibilitas dialog "Add to Library"
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedMangaToAdd by remember { mutableStateOf<JikanMangaDto?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Input Pencarian
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search Manga") },
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.search(query, context)
                }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Konten Hasil Pencarian berdasarkan State (Idle, Loading, Error, Success)
        Box(modifier = Modifier.fillMaxSize()) {
            when (val currentState = state) {
                is SearchState.Idle -> {
                    Text(
                        text = "Type a title to start searching...",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                is SearchState.Loading -> {
                    LoadingIndicator()
                }
                is SearchState.Error -> {
                    Text(
                        text = currentState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }
                is SearchState.Success -> {
                    LazyColumn {
                        items(currentState.mangaList) { manga ->
                            SearchItem(
                                manga = manga,
                                onClick = {
                                    navController?.navigate(NavRoute.DetailApi.createRoute(manga.malId))
                                },
                                onSave = {
                                    // Buka dialog untuk input metadata sebelum menyimpan
                                    selectedMangaToAdd = manga
                                    showAddDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Menangani tampilan Dialog Penambahan Manga
    if (showAddDialog && selectedMangaToAdd != null) {
        val manga = selectedMangaToAdd!!

        // Membuat entitas sementara dengan nilai default untuk ditampilkan di form dialog
        val tempEntity = MangaEntity(
            mangaId = manga.malId,
            userId = 0, // Placeholder, akan diatur otomatis oleh repository/database
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
                // Simpan manga ke database dengan metadata yang diinput user
                viewModel.saveToLibrary(
                    manga = manga,
                    status = status,
                    rating = rating,
                    volumeOwned = volume
                )
                Toast.makeText(context, "${manga.title} added to Library", Toast.LENGTH_SHORT).show()
                showAddDialog = false
            }
        )
    }
}

/**
 * Komponen UI untuk menampilkan satu item hasil pencarian dalam daftar.
 *
 * @param manga Data manga dari API.
 * @param onClick Aksi saat item diklik (buka detail).
 * @param onSave Aksi saat tombol Save diklik.
 */
@Composable
private fun SearchItem(
    manga: JikanMangaDto,
    onClick: () -> Unit,
    onSave: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = manga.images?.jpg?.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = manga.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Score: ${manga.score ?: "-"}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Status: ${manga.status ?: "-"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = onSave,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text("Save")
            }
        }
    }
}