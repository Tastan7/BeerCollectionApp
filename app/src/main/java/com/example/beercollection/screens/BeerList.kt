package com.example.beercollection.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.beercollection.model.Beer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeerListScreen(
    beers: List<Beer> = listOf(),
    onBeerSelected: (Beer) -> Unit = {},
    onBeerDeleted: (Beer) -> Unit = {},
    signOut: () -> Unit = {},
    sortByName: (Boolean) -> Unit = {},
    sortByABV: (Boolean) -> Unit = {},
    filterByName: (String) -> Unit = {},
    filterByAbv: (Double) -> Unit = {},
    onAddBeerClicked: () -> Unit = {}
) {
    var filterText by remember { mutableStateOf("") }
    var filterType by remember { mutableStateOf("Name") }
    var isNameAscending by remember { mutableStateOf(true) }
    var isABVAscending by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    var showLogoutDialog by remember { mutableStateOf(false) } // State for logout confirmation dialog

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Beer List") },
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) { // Show logout dialog on click
                        Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
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
                        if (filterText.isEmpty()) {
                            filterByName("") // Reset name filter
                            filterByAbv(0.0) // Reset ABV filter
                        } else if (filterType == "Name") {
                            filterByName(filterText) // Apply name filter
                        } else {
                            val minAbv = filterText.toDoubleOrNull()
                            if (minAbv != null) {
                                filterByAbv(minAbv) // Apply ABV filter
                            } else {
                                filterByAbv(0.0) // Reset ABV filter if input is invalid
                                errorMessage = "Please enter a valid number for ABV." // Set error message
                            }
                        }
                    },
                    label = { Text("Filter by $filterType") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = if (filterType == "Name") KeyboardType.Text else KeyboardType.Number
                    )
                )

                Button(
                    onClick = {
                        // Toggle filter type between Name and ABV
                        filterType = if (filterType == "Name") "ABV" else "Name"
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(filterType)
                }
            }

            // Error Message Display
            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
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
                    Text(if (isNameAscending) "Sort Name \u2191" else "Sort Name \u2193")
                }
                Button(onClick = {
                    sortByABV(isABVAscending) // Trigger sorting by ABV
                    isABVAscending = !isABVAscending // Toggle sorting order
                }) {
                    Text(if (isABVAscending) "Sort ABV \u2191" else "Sort ABV \u2193")
                }
            }

            // List of Beers
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier.fillMaxSize()
            ) {
                items(beers) { beer ->
                    BeerItem(
                        beer = beer,
                        onBeerSelected = onBeerSelected,
                        onBeerDeleted = onBeerDeleted
                    )
                }
            }

            // Handle empty beer list scenario
            if (beers.isEmpty()) {
                Text("No beers available. Please add some!", color = Color.Gray, modifier = Modifier.padding(16.dp))
            }
        }

        // Confirmation Dialog for Logout
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Logout Confirmation") },
                text = { Text("Are you sure you want to log out?") },
                confirmButton = {
                    Button(onClick = {
                        signOut() // Call the sign-out function
                        showLogoutDialog = false // Dismiss dialog
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(onClick = { showLogoutDialog = false }) {
                        Text("No")
                    }
                }
            )
        }
    }
}

@Composable
fun BeerItem(
    beer: Beer,
    onBeerSelected: (Beer) -> Unit = {},
    onBeerDeleted: (Beer) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { onBeerSelected(beer) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = beer.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "ABV: ${beer.abv}%") // Display beer ABV
            }
            IconButton(onClick = { onBeerDeleted(beer) }) {
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
