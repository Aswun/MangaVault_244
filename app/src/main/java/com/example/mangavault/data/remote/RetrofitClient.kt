package com.example.mangavault.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton Object untuk konfigurasi Retrofit Client.
 * Menyediakan instance [JikanApiService] untuk melakukan request ke API.
 * Dilengkapi dengan Logging Interceptor untuk debugging response API.
 */
object RetrofitClient {

    private const val BASE_URL = "https://api.jikan.moe/v4/"

    val api: JikanApiService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BASIC)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JikanApiService::class.java)
    }
}