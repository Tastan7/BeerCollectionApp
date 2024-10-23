package com.example.beercollection

sealed class NavRoutes(val route: String) {
    data object BeerList : NavRoutes("beer_list")
    data object BeerDetails : NavRoutes("beer_details/{beerId}") {
        fun createRoute(beerId: Int) = "beer_details/$beerId"
    }
    data object BeerAdd : NavRoutes("beer_add")
}
