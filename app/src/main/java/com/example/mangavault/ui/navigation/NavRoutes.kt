package com.example.mangavault.ui.navigation

sealed class NavRoute(val route: String) {
    data object Login : NavRoute("login")
    data object Library : NavRoute("library")
    data object Search : NavRoute("search")
    data object About : NavRoute("about")
    data object Setting : NavRoute("setting")

}
