package com.example.mangavault.data.repository

import com.example.mangavault.data.remote.RetrofitClient
import com.example.mangavault.data.remote.dto.JikanMangaDto

class SearchRepository {

    suspend fun searchManga(query: String): List<JikanMangaDto> {
        return RetrofitClient.api
            .searchManga(query)
            .data
    }
}