package com.example.mangavault.data.remote.dto

import com.google.gson.annotations.SerializedName

// Menggunakan Generic <T> agar bisa dipakai untuk List maupun Single Object
data class JikanResponse<T>(
    @SerializedName("data")
    val data: T,

    // Pagination biasanya hanya ada di search, jadi kita buat nullable
    @SerializedName("pagination")
    val pagination: PaginationDto? = null
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

data class PaginationDto(
    @SerializedName("last_visible_page")
    val lastVisiblePage: Int,
    @SerializedName("has_next_page")
    val hasNextPage: Boolean
)