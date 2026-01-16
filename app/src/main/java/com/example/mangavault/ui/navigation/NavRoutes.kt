package com.example.mangavault.ui.navigation

sealed class NavRoute(val route: String) {
    data object Login : NavRoute("login")
    data object Library : NavRoute("library")
    data object Search : NavRoute("search")
    data object About : NavRoute("about")
    data object Setting : NavRoute("setting")

    // Tambahan untuk Detail
    // Kita gunakan format "route/{id}"
    data object DetailApi : NavRoute("detail_api/{mangaId}") {
        fun createRoute(mangaId: Int) = "detail_api/$mangaId"
    }
    data object DetailLocal : NavRoute("detail_local/{mangaId}") {
        fun createRoute(mangaId: Int) = "detail_local/$mangaId"
    }
}