package com.example.beercollection.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beercollection.model.Beer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeerDetailsScreen(
    beer: Beer,
    onUpdateClicked: (Int, Beer) -> Unit, // Update callback with beer ID
    onBackPressed: () -> Unit

) {
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(beer.name) }
    var brewery by remember { mutableStateOf(beer.brewery) }
    var style by remember { mutableStateOf(beer.style) }
    var abvStr by remember { mutableStateOf(beer.abv.toString()) }
    var volumeStr by remember { mutableStateOf(beer.volume.toString()) }
    var amountStr by remember { mutableStateOf(beer.howMany.toString()) }

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Helper function to validate the input
    fun validateInputs(): Boolean {
        return when {
            abvStr.toDoubleOrNull() == null -> {
                errorMessage = "ABV must be a valid number."
                Log.e("BeerDetailsScreen", "Invalid ABV: $abvStr")
                false
            }
            volumeStr.toDoubleOrNull() == null -> {
                errorMessage = "Volume must be a valid number."
                Log.e("BeerDetailsScreen", "Invalid Volume: $volumeStr")
                false
            }
            amountStr.toIntOrNull() == null -> {
                errorMessage = "Amount must be a valid integer."
                Log.e("BeerDetailsScreen", "Invalid Amount: $amountStr")
                false
            }
            name.isBlank() || brewery.isBlank() || style.isBlank() -> {
                errorMessage = "All fields must be filled."
                Log.e("BeerDetailsScreen", "Missing field values")
                false
            }
            else -> {
                showError = false
                true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Beer Details") },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Spot for the picture
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(bottom = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Picture here", color = Color.Gray)
                    }
                }

                // Edit button slightly below and to the right
                IconButton(
                    onClick = { isEditing = !isEditing },
                    modifier = Modifier
                        .offset(x = (100).dp, y = 50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = Color.Blue
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (showError) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }

            if (isEditing) {
                DetailEditableField(value = name, onValueChange = { name = it }, label = "Name")
                DetailEditableField(value = brewery, onValueChange = { brewery = it }, label = "Brewery")
                DetailEditableField(value = style, onValueChange = { style = it }, label = "Style")
                DetailEditableField(value = abvStr, onValueChange = { abvStr = it }, label = "ABV", KeyboardType.Number)
                DetailEditableField(value = volumeStr, onValueChange = { volumeStr = it }, label = "Volume", KeyboardType.Number)
                DetailEditableField(value = amountStr, onValueChange = { amountStr = it }, label = "Amount", KeyboardType.Number)

                Button(
                    onClick = {
                        if (validateInputs()) {
                            try {
                                val updatedBeer = beer.copy(
                                    name = name,
                                    brewery = brewery,
                                    style = style,
                                    abv = abvStr.toDouble(),
                                    volume = volumeStr.toDouble(),
                                    howMany = amountStr.toInt(),
                                    pictureUrl = beer.pictureUrl ?: "" // Ensure pictureUrl is not null
                                )
                                onUpdateClicked(beer.id, updatedBeer)
                                isEditing = false
                            } catch (e: Exception) {
                                showError = true
                                errorMessage = "An error occurred: ${e.message}"
                                Log.e("BeerDetailsScreen", "Update failed", e)
                            }
                        } else {
                            showError = true
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Apply Changes")
                }
            } else {
                Text(
                    text = beer.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                DetailRow(label = "Brewery", value = brewery)
                HorizontalDivider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                DetailRow(label = "Style", value = style)
                HorizontalDivider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                DetailRow(label = "ABV", value = "${abvStr}%")
                HorizontalDivider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                DetailRow(label = "Volume", value = "$volumeStr ml")
                HorizontalDivider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                DetailRow(label = "Amount", value = amountStr)
            }
        }
    }
}

@Composable
fun DetailEditableField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth().padding(4.dp)
    )
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Text(text = value, fontWeight = FontWeight.Bold)
    }
}

@Suppress("DEPRECATION")
@Composable
fun HorizontalDivider(color: Color, thickness: Dp, modifier: Modifier = Modifier) {
    Divider(modifier = modifier, thickness = thickness, color = color)
}

@Preview
@Composable
fun BeerDetailsPreview() {
    BeerDetailsScreen(
        beer = Beer(
            id = 1,
            user = "User1",
            brewery = "Tuborg",
            name = "Grøn",
            style = "Pilsner",
            abv = 5.0,
            volume = 500.0,
            pictureUrl = "",
            howMany = 10
        ),
        onUpdateClicked = { _, _ -> },
        onBackPressed = {}
    )
}
