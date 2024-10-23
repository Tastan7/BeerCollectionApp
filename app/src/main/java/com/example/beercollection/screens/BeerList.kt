package com.example.beercollection.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.beercollection.model.Beer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeerListScreen(
    beers: List<Beer> = listOf(), // List of beers fetched from API
    onBeerSelected: (Beer) -> Unit = {}, // Callback when a beer is selected
    onBeerDeleted: (Beer) -> Unit = {}, // Callback when a beer is deleted
    onMenuClicked: () -> Unit = {}, // Callback when the menu is clicked
    sortByName: (Boolean) -> Unit = {}, // Sorting function for beer name
    sortByABV: (Boolean) -> Unit = {}, // Sorting function for ABV
    filterByName: (String) -> Unit = {}, // Filtering function for beer name
    onAddBeerClicked: () -> Unit = {} // New parameter for handling add button click

) {

    Log.d("BeerListScreen", "Rendering BeerListScreen with ${beers.size} beers")

    var filterText by remember { mutableStateOf("") } // State for filter text
    var isNameAscending by remember { mutableStateOf(true) } // State for sorting name in ascending order
    var isABVAscending by remember { mutableStateOf(true) } // State for sorting ABV in ascending order

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Beer List") }, // Top bar title
                actions = {
                    IconButton(onClick = { onMenuClicked() }) { // Menu button action
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            // Floating Action Button for adding a new beer
            FloatingActionButton(
                onClick = onAddBeerClicked,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Beer")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()) {

            // Filter Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = filterText,
                    onValueChange = {
                        filterText = it
                        filterByName(filterText) // Trigger filter by name function when text changes
                    },
                    label = { Text("Filter by Name") },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = { filterByName(filterText) }, // Filter button click action
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Filter")
                }
            }

            // Sorting Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    sortByName(isNameAscending) // Trigger sorting by name
                    isNameAscending = !isNameAscending // Toggle sorting order
                }) {
                    Text(if (isNameAscending) "Sort Name \u2191" else "Sort Name \u2193") // Show sorting direction
                }
                Button(onClick = {
                    sortByABV(isABVAscending) // Trigger sorting by ABV
                    isABVAscending = !isABVAscending // Toggle sorting order
                }) {
                    Text(if (isABVAscending) "Sort ABV \u2191" else "Sort ABV \u2193") // Show sorting direction
                }
            }

            // List of Beers
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier.fillMaxSize()
            ) {
                items(beers) { beer ->
                    Log.d("BeerListScreen", "Displaying beer: ${beer.name}")
                    BeerItem(
                        beer = beer,
                        onBeerSelected = onBeerSelected, // Handle beer selection
                        onBeerDeleted = onBeerDeleted // Handle beer deletion
                    )
                }
            }
        }
    }
}

@Composable
fun BeerItem(
    beer: Beer,
    onBeerSelected: (Beer) -> Unit = {}, // Callback for selecting a beer
    onBeerDeleted: (Beer) -> Unit = {} // Callback for deleting a beer
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { onBeerSelected(beer) } // Handle beer card click
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = beer.name, style = MaterialTheme.typography.titleMedium) // Beer name
                Text(text = "ABV: ${beer.abv}%") // Display beer ABV
            }
            IconButton(onClick = { onBeerDeleted(beer) }) { // Delete button
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }
    }
}

@Preview
@Composable
fun BeerListScreenPreview() {
    BeerListScreen(
        beers = listOf(
            Beer(id = 1, user = "User1", brewery = "Tuborg", name = "Grøn", style = "Pilsner", abv = 5.0, volume = 500.0, pictureUrl = "", howMany = 10),
            Beer(id = 2, user = "User2", brewery = "Carlsberg", name = "Classic", style = "Lager", abv = 4.6, volume = 330.0, pictureUrl = "", howMany = 5),
            Beer(id = 3, user = "User3", brewery = "Lervig", name = "POG", style = "Barley Wine", abv = 13.5, volume = 375.0, pictureUrl = "", howMany = 1),
            Beer(id = 4, user = "User4", brewery = "Det Lille Bryggeri", name = "Big Mash", style = "Imperial Stout", abv = 16.2, volume = 500.0, pictureUrl = "", howMany = 1),
            Beer(id = 5, user = "User5", brewery = "Nerdbrewing", name = "OMEGALUL", style = "Imperial Stout", abv = 11.6, volume = 330.0, pictureUrl = "", howMany = 4)
        ),
        onAddBeerClicked = {}
    )
}
