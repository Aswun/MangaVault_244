package com.example.mangavault.data.repository

import com.example.mangavault.data.remote.RetrofitClient
import com.example.mangavault.data.remote.dto.JikanMangaDto

/**
 * Repository yang menangani pencarian data Manga dari sumber eksternal (Jikan API).
 */
class SearchRepository {

    /**
     * Mengirim request pencarian ke API.
     *
     * @param query Kata kunci pencarian.
     * @return List hasil pencarian dalam format DTO.
     */
    suspend fun searchManga(query: String): List<JikanMangaDto> {
        return RetrofitClient.api
            .searchManga(query)
            .data
    }
}