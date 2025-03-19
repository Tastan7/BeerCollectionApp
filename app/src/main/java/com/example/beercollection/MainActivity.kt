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
import com.example.beercollection.model.AuthenticationViewModel
import com.example.beercollection.repository.AuthenticationRepository
import com.example.beercollection.screens.Authentication
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
    val authRepository = AuthenticationRepository() // Create repository instance
    val authViewModel = AuthenticationViewModel(authRepository) // Create ViewModel instance with the repository

    NavHost(navController = navController, startDestination = if (authViewModel.user != null) NavRoutes.BeerList.route else NavRoutes.Authentication.route) {
        // Authentication screen
        composable(NavRoutes.Authentication.route) {
            Authentication(
                user = authViewModel.user,

                message = authViewModel.message.value,

                signIn = { email, password -> authViewModel.signIn(email, password) },

                register = { email, password -> authViewModel.register(email, password) },

                navigateToNextScreen = { navController.navigate(NavRoutes.BeerList.route) }
            )
        }

        // Beer List screen
        composable(NavRoutes.BeerList.route) {
            BeerListScreen(
                beers = beersViewModel.beers.value,
                onBeerSelected = { beer -> navController.navigate(NavRoutes.BeerDetails.createRoute(beer.id)) },

                onBeerDeleted = { beer -> beersViewModel.removeBeer(beer.id) },

                signOut = {
                    authViewModel.signOut() // Call sign-out logic
                    navController.navigate(NavRoutes.Authentication.route) // Navigate to authentication screen
                },
                sortByName = { ascending -> beersViewModel.sortBeersByName(ascending) },

                sortByABV = { ascending -> beersViewModel.sortBeersByAbv(ascending) },

                filterByName = { nameFragment -> beersViewModel.filterBeersByName(nameFragment) },

                filterByAbv = { minAbv -> beersViewModel.filterBeersByAbv(minAbv) },

                onAddBeerClicked = { navController.navigate(NavRoutes.BeerAdd.route) }
            )
        }

        // Beer Details screen
        composable(
            NavRoutes.BeerDetails.route,
            arguments = listOf(navArgument(name = "beerId") { type = NavType.IntType })
        ) { backStackEntry ->
            val beerId = backStackEntry.arguments?.getInt("beerId")
            val beer = beersViewModel.beers.value.find { it.id == beerId }
            if (beer != null) {
                BeerDetailsScreen(
                    beer = beer,

                    onUpdateClicked = { id, updatedBeer -> beersViewModel.updateBeer(id, updatedBeer) },

                    onBackPressed = { navController.popBackStack() }
                )
            }
        }

        // Beer Add screen
        composable(NavRoutes.BeerAdd.route) {
            BeerAddScreen(
                onAddBeer = { newBeer ->
                    beersViewModel.addBeer(newBeer)
                    navController.popBackStack()
                },
                onNavigateBack = { navController.popBackStack() }
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
