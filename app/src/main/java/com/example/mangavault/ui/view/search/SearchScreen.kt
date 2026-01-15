package com.example.mangavault.ui.view.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mangavault.data.remote.dto.JikanMangaDto
import com.example.mangavault.ui.viewmodel.search.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel
) {
    val results by viewModel.results.collectAsState()
    var query by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search Manga") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.search(query) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(results) { manga ->
                SearchItem(
                    manga = manga,
                    onSave = { viewModel.saveToLibrary(manga) }
                )
            }
        }
    }
}

@Composable
private fun SearchItem(
    manga: JikanMangaDto,
    onSave: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onSave() }
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = manga.images?.jpg?.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(manga.title, style = MaterialTheme.typography.titleMedium)
                Text("Tap to save", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
