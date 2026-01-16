package com.example.mangavault.data.remote

import com.example.mangavault.data.remote.dto.JikanResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface JikanApiService {

    @GET("manga")
    suspend fun searchManga(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20
    ): JikanResponse
}