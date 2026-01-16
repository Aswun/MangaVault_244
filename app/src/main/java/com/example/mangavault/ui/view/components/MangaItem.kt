package com.example.mangavault.ui.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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
            // Menambahkan padding antar item grid ditangani oleh LazyVerticalGrid,
            // tapi kita beri sedikit margin vertikal jika perlu
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            // 1. Cover Image (Bagian Atas)
            // Menggunakan Box atau langsung AsyncImage dengan height fix agar seragam
            AsyncImage(
                model = manga.imageUrl,
                contentDescription = "Cover of ${manga.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp), // Tinggi fix agar grid terlihat rapi
                contentScale = ContentScale.Crop // Memotong gambar agar memenuhi kotak
            )

            // 2. Informasi Manga (Bagian Bawah)
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // Judul
                Text(
                    text = manga.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2, // Batasi 2 baris agar tidak terlalu panjang
                    overflow = TextOverflow.Ellipsis // Tambahkan "..." jika kepanjangan
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Status & Rating (Baris kecil)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = manga.status,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.weight(1f)
                    )

                    if (manga.rating != null && manga.rating > 0) {
                        Text(
                            text = "★ ${manga.rating}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}