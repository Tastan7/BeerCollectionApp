package com.example.beercollection

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.beercollection.model.BeersViewModel
import com.example.beercollection.screens.BeerAddScreen
import com.example.beercollection.screens.BeerDetailsScreen
import com.example.beercollection.screens.BeerListScreen
import com.example.beercollection.ui.theme.BeerCollectionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeerCollectionTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val beersViewModel: BeersViewModel = viewModel()

    NavHost(navController = navController, startDestination = NavRoutes.BeerList.route) {
        composable(NavRoutes.BeerList.route) {
            BeerListScreen(
                beers = beersViewModel.beers.value,
                onBeerSelected = { beer ->
                    navController.navigate(NavRoutes.BeerDetails.createRoute(beer.id))
                },
                onBeerDeleted = { beer ->
                    beersViewModel.removeBeer(beer.id)
                },
                sortByName = { ascending ->
                    beersViewModel.sortBeersByName(ascending)
                },
                sortByABV = { ascending ->
                    beersViewModel.sortBeersByAbv(ascending)
                },
                filterByName = { nameFragment ->
                    beersViewModel.filterBeersByName(nameFragment)
                },
                onAddBeerClicked = {
                    navController.navigate(NavRoutes.BeerAdd.route)
                }
            )
        }
        composable(
            NavRoutes.BeerDetails.route,
            arguments = listOf(navArgument(name = "beerId") { type = NavType.IntType })
        ) { backStackEntry ->
            val beerId = backStackEntry.arguments?.getInt("beerId")
            val beer = beersViewModel.beers.value.find { it.id == beerId }
            if (beer != null) {
                BeerDetailsScreen(
                    beer = beer,
                    onUpdateClicked = { id, updatedBeer ->
                        beersViewModel.updateBeer(id, updatedBeer)
                    },
                    onBackPressed = {
                        navController.popBackStack()
                    }
                )
            }
        }
        composable(NavRoutes.BeerAdd.route) {
            BeerAddScreen(
                onAddBeer = { newBeer ->
                    beersViewModel.addBeer(newBeer)
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    BeerCollectionTheme {
        MainScreen()
    }
}
