package com.example.mangavault.data.remote.dto

import com.google.gson.annotations.SerializedName

data class JikanResponse(
    @SerializedName("data")
    val data: List<JikanMangaDto>
)

data class JikanMangaDto(
    @SerializedName("mal_id")
    val malId: Int,

    val title: String,

    @SerializedName("images")
    val images: ImageWrapper?
)

data class ImageWrapper(
    @SerializedName("jpg")
    val jpg: ImageJpg?
)

data class ImageJpg(
    @SerializedName("image_url")
    val imageUrl: String?
)
