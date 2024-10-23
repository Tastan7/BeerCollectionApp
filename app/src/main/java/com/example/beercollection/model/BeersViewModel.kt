package com.example.beercollection.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.beercollection.repository.BeersRepository

class BeersViewModel : ViewModel() {

    private val repository = BeersRepository()

    // Expose repository data as State to be observed in the UI
    val beers: State<List<Beer>> = repository.beers
    val errorMessage: State<String> = repository.errorMessage
    val isLoadingBeers: State<Boolean> = repository.isLoadingBeers

    init {
        getBeers()
    }

    // Fetch all beers from the repository
    fun getBeers() {
        repository.getBeers()
    }

    // Add a beer via the repository
    fun addBeer(beer: Beer) {
        repository.addBeer(beer)
    }

    // Update a beer via the repository
    fun updateBeer(beerId: Int, beer: Beer) {
        repository.updateBeer(beerId, beer)
    }

    // Remove a beer via the repository
    fun removeBeer(beerId: Int) {
        repository.deleteBeer(beerId)
    }

    // Sorting methods
    fun sortBeersByName(ascending: Boolean) {
        repository.sortBeersByName(ascending)
    }

    fun sortBeersByBrewery(ascending: Boolean) {
        repository.sortBeersByBrewery(ascending)
    }

    fun sortBeersByAbv(ascending: Boolean) {
        repository.sortBeersByAbv(ascending)
    }

    // Filtering methods
    fun filterBeersByName(nameFragment: String) {
        repository.filterBeersByName(nameFragment)
    }

    fun filterBeersByBrewery(breweryFragment: String) {
        repository.filterBeersByBrewery(breweryFragment)
    }

    fun filterBeersByAbv(minAbv: Double) {
        repository.filterBeersByAbv(minAbv)
    }
}
