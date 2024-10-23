package com.example.beercollection.repository

import com.example.beercollection.model.Beer
import retrofit2.Call
import retrofit2.http.*

interface BeersService {

    // Fetch all beers
    @GET("beers")
    fun getAllBeers(): Call<List<Beer>>

    // Get beer by ID (This is optional only if used)
    @GET("beers/{beerId}")
    fun getBeerById(@Path("beerId") beerId: Int): Call<Beer>

    // Add a new beer
    @POST("beers")
    fun addBeer(@Body beer: Beer): Call<Beer>

    // Delete a beer by ID
    @DELETE("beers/{id}")
    fun deleteBeer(@Path("id") id: Int): Call<Beer>

    // Update an existing beer by ID
    @PUT("beers/{id}")
    fun updateBeer(@Path("id") id: Int, @Body beer: Beer): Call<Beer>
}