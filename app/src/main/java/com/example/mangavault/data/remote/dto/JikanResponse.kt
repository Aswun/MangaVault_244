package com.example.mangavault.data.remote.dto

import com.google.gson.annotations.SerializedName

data class JikanResponse(
    @SerializedName("data")
    val data: List<JikanMangaDto>
)

// Class baru untuk respons single detail (jika nanti dibutuhkan fetch by ID)
data class JikanSingleResponse(
    @SerializedName("data")
    val data: JikanMangaDto
)

data class JikanMangaDto(
    @SerializedName("mal_id")
    val malId: Int,

    val title: String,

    @SerializedName("images")
    val images: ImageWrapper?,

    // Field tambahan untuk detail lengkap
    val synopsis: String?,
    val type: String?,
    val status: String?,
    val score: Double?
)

data class ImageWrapper(
    @SerializedName("jpg")
    val jpg: ImageJpg?
)

data class ImageJpg(
    @SerializedName("image_url")
    val imageUrl: String?,

    @SerializedName("large_image_url")
    val largeImageUrl: String?
)