package com.example.mangavault.ui.navigation

/**
 * Sealed class untuk mendefinisikan rute navigasi dalam aplikasi.
 * Menyediakan rute statis dan dinamis (dengan argumen).
 */
sealed class NavRoute(val route: String) {
    data object Login : NavRoute("login")
    data object Library : NavRoute("library")
    data object Search : NavRoute("search")
    data object About : NavRoute("about")
    data object Setting : NavRoute("setting")

    /**
     * Rute untuk detail manga dari API.
     * @param mangaId ID Manga dari Jikan API.
     */
    data object DetailApi : NavRoute("detail_api/{mangaId}") {
        fun createRoute(mangaId: Int) = "detail_api/$mangaId"
    }

    /**
     * Rute untuk detail manga lokal (Library).
     * @param mangaId ID Manga dari database lokal.
     */
    data object DetailLocal : NavRoute("detail_local/{mangaId}") {
        fun createRoute(mangaId: Int) = "detail_local/$mangaId"
    }
}