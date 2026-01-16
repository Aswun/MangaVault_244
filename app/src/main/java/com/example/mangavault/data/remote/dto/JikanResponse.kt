package com.example.mangavault.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Data class wrapper untuk respons API Jikan.
 * Menggunakan Generic T agar fleksibel untuk berbagai tipe data.
 */
data class JikanResponse<T>(
    @SerializedName("data")
    val data: T,

    @SerializedName("pagination")
    val pagination: PaginationDto? = null
)

/**
 * Wrapper khusus untuk respons single object (detail manga).
 */
data class JikanSingleResponse(
    @SerializedName("data")
    val data: JikanMangaDto
)

/**
 * Data Transfer Object (DTO) untuk merepresentasikan Manga dari API.
 */
data class JikanMangaDto(
    @SerializedName("mal_id")
    val malId: Int,

    val title: String,

    @SerializedName("images")
    val images: ImageWrapper?,

    val synopsis: String?,
    val type: String?,
    val status: String?,
    val score: Double?
)

/**
 * Wrapper untuk struktur JSON images.
 */
data class ImageWrapper(
    @SerializedName("jpg")
    val jpg: ImageJpg?
)

/**
 * Wrapper untuk URL gambar JPG.
 */
data class ImageJpg(
    @SerializedName("image_url")
    val imageUrl: String?,

    @SerializedName("large_image_url")
    val largeImageUrl: String?
)

/**
 * Informasi pagination dari API.
 */
data class PaginationDto(
    @SerializedName("last_visible_page")
    val lastVisiblePage: Int,
    @SerializedName("has_next_page")
    val hasNextPage: Boolean
)