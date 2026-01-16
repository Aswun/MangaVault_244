package com.example.mangavault.ui.view.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

/**
 * Dialog Konfirmasi untuk aksi penghapusan data.
 * Mencegah penghapusan tidak sengaja sesuai REQ-SAFE-01.
 *
 * @param title Judul item yang akan dihapus.
 * @param onConfirm Callback jika user menekan tombol Delete.
 * @param onDismiss Callback jika user membatalkan.
 */
@Composable
fun ConfirmDeleteDialog(
    title: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Delete Manga")
        },
        text = {
            Text("Are you sure you want to delete \"$title\" from your library?")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}