package com.example.mangavault.ui.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mangavault.data.local.entity.MangaEntity

/**
 * Dialog Komponen untuk Mengedit atau Menambahkan Metadata Manga.
 * Menyediakan form input untuk Status Baca, Volume Dimiliki, dan Rating.
 * Melakukan validasi input rating agar berada di rentang 1-10.
 *
 * @param manga Objek manga yang akan diedit (atau template baru).
 * @param onDismiss Callback saat dialog ditutup.
 * @param onSave Callback saat tombol Save ditekan, mengembalikan nilai inputan.
 */
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

    // Fungsi Validasi Rating (1-10)
    fun validateRating(input: String): Boolean {
        if (input.isBlank()) return true // Boleh kosong (null)
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