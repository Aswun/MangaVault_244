package com.example.mangavault.ui.view.search

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mangavault.data.remote.dto.JikanMangaDto
import com.example.mangavault.ui.view.components.LoadingIndicator
import com.example.mangavault.ui.navigation.NavRoute
import com.example.mangavault.ui.viewmodel.search.SearchState
import com.example.mangavault.ui.viewmodel.search.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    navController: NavController? = null // Tambahkan NavController untuk navigasi ke detail
) {
    val state by viewModel.state.collectAsState()
    var query by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        /* SEARCH BAR */
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search Manga") },
            trailingIcon = {
                IconButton(onClick = { viewModel.search(query) }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        /* CONTENT BASED ON STATE */
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
                                    // Navigasi ke Detail Screen (menggunakan route yang kita buat sebelumnya)
                                    navController?.navigate(NavRoute.DetailApi.createRoute(manga.malId))
                                },
                                onSave = { viewModel.saveToLibrary(manga) }
                            )
                        }
                    }
                }
            }
        }
    }
}

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

            // Tombol Quick Save (Opsional)
            Button(
                onClick = onSave,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text("Save")
            }
        }
    }
}