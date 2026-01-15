package com.example.mangavault.ui.view.library

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mangavault.data.local.entity.MangaEntity

@Composable
fun EditMangaDialog(
    manga: MangaEntity,
    onDismiss: () -> Unit,
    onSave: (
        status: String,
        rating: Int?,
        volumeOwned: Int
    ) -> Unit
) {
    var status by remember { mutableStateOf(manga.status) }
    var rating by remember { mutableStateOf(manga.rating?.toString() ?: "") }
    var volume by remember { mutableStateOf(manga.volumeOwned.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Manga") },
        text = {
            Column {

                StatusDropdown(
                    selected = status,
                    onSelect = { status = it }
                )

                OutlinedTextField(
                    value = volume,
                    onValueChange = { volume = it },
                    label = { Text("Volume Owned") }
                )

                OutlinedTextField(
                    value = rating,
                    onValueChange = { rating = it },
                    label = { Text("Rating (1–10)") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    status,
                    rating.toIntOrNull(),
                    volume.toIntOrNull() ?: 0
                )
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
