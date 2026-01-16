package com.example.mangavault.ui.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
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
    var ratingString by remember { mutableStateOf(manga.rating?.toString() ?: "") }
    var volumeString by remember { mutableStateOf(manga.volumeOwned.toString()) }

    var isRatingError by remember { mutableStateOf(false) }

    // Validasi Rating: Harus 1-10 atau kosong (null)
    fun validateRating(input: String): Boolean {
        if (input.isBlank()) return true
        val num = input.toIntOrNull()
        return num != null && num in 1..10
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Manga") },
        text = {
            Column {
                StatusDropdown(
                    currentStatus = status,
                    onStatusSelected = { status = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = volumeString,
                    onValueChange = { if (it.all { char -> char.isDigit() }) volumeString = it },
                    label = { Text("Volume Owned") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = ratingString,
                    onValueChange = {
                        ratingString = it
                        isRatingError = !validateRating(it)
                    },
                    label = { Text("Rating (1–10)") },
                    isError = isRatingError,
                    supportingText = {
                        if (isRatingError) {
                            Text("Rating must be between 1 and 10")
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        status,
                        ratingString.toIntOrNull(),
                        volumeString.toIntOrNull() ?: 0
                    )
                },
                // Disable tombol jika ada error validasi
                enabled = !isRatingError
            ) {
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