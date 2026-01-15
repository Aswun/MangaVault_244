package com.example.mangavault.ui.view.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mangavault.data.local.entity.MangaEntity

@Composable
fun MangaItem(
    manga: MangaEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable { onClick() }
                .padding(12.dp)
        ) {

            Text(
                text = manga.title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Status: ${manga.status}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Volume Owned: ${manga.volumeOwned}",
                style = MaterialTheme.typography.bodyMedium
            )

            manga.rating?.let {
                Text(
                    text = "Rating: $it / 10",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onDelete
            ) {
                Text("Delete")
            }
        }
    }
}
