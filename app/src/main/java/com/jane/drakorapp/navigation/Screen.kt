package com.jane.drakorapp.navigation

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Detail: Screen("home/{DrakorId}") {
        fun createRoute(DrakorId: Int) = "home/$DrakorId"
    }
    object Favorite: Screen("favorite")
    object Profile: Screen("profile")
}