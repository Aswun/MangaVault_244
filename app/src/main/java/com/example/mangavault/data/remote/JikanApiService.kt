package com.example.mangavault.data.remote

import com.example.mangavault.data.remote.dto.JikanMangaDto
import com.example.mangavault.data.remote.dto.JikanResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface untuk definisi endpoint Jikan REST API.
 * Digunakan oleh Retrofit untuk melakukan HTTP Request.
 */
interface JikanApiService {

    /**
     * Mencari manga berdasarkan kata kunci (query).
     * Endpoint: GET /manga?q={query}&limit={limit}
     *
     * @param query Kata kunci pencarian judul manga.
     * @param limit Batas jumlah hasil (default 20).
     * @return Response yang berisi daftar manga.
     */
    @GET("manga")
    suspend fun searchManga(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20
    ): JikanResponse<List<JikanMangaDto>>
}