package com.example.beercollection.repository

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.beercollection.model.Beer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BeersRepository {
    private val baseUrl = "https://anbo-restbeer.azurewebsites.net/api/"
    private val beersService: BeersService

    // State to hold the current list of beers
    val beers: MutableState<List<Beer>> = mutableStateOf(listOf())
    val isLoadingBeers = mutableStateOf(false)
    val errorMessage = mutableStateOf("")

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        beersService = retrofit.create(BeersService::class.java)
    }

    // Fetch all beers
    fun getBeers() {
        Log.d("BeersRepository", "Fetching beers from API")
        isLoadingBeers.value = true
        beersService.getAllBeers().enqueue(object : Callback<List<Beer>> {
            override fun onResponse(call: Call<List<Beer>>, response: Response<List<Beer>>) {
                isLoadingBeers.value = false
                if (response.isSuccessful) {
                    beers.value = response.body() ?: emptyList()
                    errorMessage.value = ""
                    Log.d("BeersRepository", "Fetched beers: ${beers.value}")
                } else {
                    val message = "Error ${response.code()}: ${response.message()}"
                    errorMessage.value = message
                    Log.e("BeersRepository", message)
                }
            }

            override fun onFailure(call: Call<List<Beer>>, t: Throwable) {
                isLoadingBeers.value = false
                val message = t.message ?: "Unknown error. Check your connection."
                errorMessage.value = message
                Log.e("BeersRepository", message)
            }
        })
    }

    // Add a new beer
    fun addBeer(beer: Beer) {
        Log.d("BeersRepository", "Attempting to add beer: $beer")
        beersService.addBeer(beer).enqueue(object : Callback<Beer> {
            override fun onResponse(call: Call<Beer>, response: Response<Beer>) {
                if (response.isSuccessful) {
                    Log.d("BeersRepository", "Successfully added beer: ${response.body()}")
                    getBeers() // Refresh the list after adding a beer
                    errorMessage.value = ""
                } else {
                    val message = "Error ${response.code()}: ${response.message()}"
                    errorMessage.value = message
                    Log.e("BeersRepository", message)
                }
            }

            override fun onFailure(call: Call<Beer>, t: Throwable) {
                val message = t.message ?: "Unknown error. Could not add beer."
                errorMessage.value = message
                Log.e("BeersRepository", message)
            }
        })
    }

    // Update a beer
    fun updateBeer(beerId: Int, beer: Beer) {
        Log.d("BeersRepository", "Attempting to update beer with ID: $beerId")
        beersService.updateBeer(beerId, beer).enqueue(object : Callback<Beer> {
            override fun onResponse(call: Call<Beer>, response: Response<Beer>) {
                if (response.isSuccessful) {
                    Log.d("BeersRepository", "Successfully updated beer: ${response.body()}")
                    getBeers() // Refresh the list after updating a beer
                    errorMessage.value = ""
                } else {
                    val message = "Error ${response.code()}: ${response.message()}"
                    errorMessage.value = message
                    Log.e("BeersRepository", message)
                }
            }

            override fun onFailure(call: Call<Beer>, t: Throwable) {
                val message = t.message ?: "Unknown error. Could not update beer."
                errorMessage.value = message
                Log.e("BeersRepository", message)
            }
        })
    }

    // Delete a beer by ID
    fun deleteBeer(beerId: Int) {
        Log.d("BeersRepository", "Attempting to delete beer with ID: $beerId")
        beersService.deleteBeer(beerId).enqueue(object : Callback<Beer> {
            override fun onResponse(call: Call<Beer>, response: Response<Beer>) {
                if (response.isSuccessful) {
                    Log.d("BeersRepository", "Successfully deleted beer: ${response.body()}")
                    getBeers() // Refresh the list after deleting a beer
                    errorMessage.value = ""
                } else {
                    val message = "Error ${response.code()}: ${response.message()}"
                    errorMessage.value = message
                    Log.e("BeersRepository", message)
                }
            }

            override fun onFailure(call: Call<Beer>, t: Throwable) {
                val message = t.message ?: "Unknown error. Could not delete beer."
                errorMessage.value = message
                Log.e("BeersRepository", message)
            }
        })
    }

    // Sorting beers by name (ascending/descending)
    fun sortBeersByName(ascending: Boolean) {
        Log.d("BeersRepository", "Sorting beers by name. Ascending: $ascending")
        beers.value = if (ascending) {
            beers.value.sortedBy { it.name }
        } else {
            beers.value.sortedByDescending { it.name }
        }
    }

    // Sorting beers by brewery (ascending/descending)
    fun sortBeersByBrewery(ascending: Boolean) {
        Log.d("BeersRepository", "Sorting beers by brewery. Ascending: $ascending")
        beers.value = if (ascending) {
            beers.value.sortedBy { it.brewery }
        } else {
            beers.value.sortedByDescending { it.brewery }
        }
    }

    // Sorting beers by ABV (ascending/descending)
    fun sortBeersByAbv(ascending: Boolean) {
        Log.d("BeersRepository", "Sorting beers by ABV. Ascending: $ascending")
        beers.value = if (ascending) {
            beers.value.sortedBy { it.abv }
        } else {
            beers.value.sortedByDescending { it.abv }
        }
    }

    // Filtering beers by name fragment
    fun filterBeersByName(nameFragment: String) {
        Log.d("BeersRepository", "Filtering beers by name fragment: $nameFragment")
        if (nameFragment.isEmpty()) {
            getBeers()
        } else {
            beers.value = beers.value.filter { it.name.contains(nameFragment, ignoreCase = true) }
        }
    }

    // Filtering beers by brewery fragment
    fun filterBeersByBrewery(breweryFragment: String) {
        Log.d("BeersRepository", "Filtering beers by brewery fragment: $breweryFragment")
        if (breweryFragment.isEmpty()) {
            getBeers()
        } else {
            beers.value = beers.value.filter { it.brewery.contains(breweryFragment, ignoreCase = true) }
        }
    }

    // Filtering beers by minimum ABV value
    fun filterBeersByAbv(minAbv: Double) {
        Log.d("BeersRepository", "Filtering beers by minimum ABV: $minAbv")
        beers.value = beers.value.filter { it.abv >= minAbv }
    }
}
