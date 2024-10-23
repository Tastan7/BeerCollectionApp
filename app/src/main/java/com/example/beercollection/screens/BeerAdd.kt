package com.example.beercollection.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.beercollection.model.Beer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeerAddScreen(
    onAddBeer: (Beer) -> Unit,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var brewery by remember { mutableStateOf("") }
    var style by remember { mutableStateOf("") }
    var abvStr by remember { mutableStateOf("") }
    var volumeStr by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("") }

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    fun validateInputs(): Boolean {
        return when {
            name.isBlank() || brewery.isBlank() || style.isBlank() || abvStr.isBlank() || volumeStr.isBlank() || amountStr.isBlank() -> {
                errorMessage = "All fields must be filled."
                false
            }
            abvStr.toDoubleOrNull() == null -> {
                errorMessage = "ABV must be a valid number."
                false
            }
            volumeStr.toDoubleOrNull() == null -> {
                errorMessage = "Volume must be a valid number."
                false
            }
            amountStr.toIntOrNull() == null -> {
                errorMessage = "Amount must be a valid integer."
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
                title = { Text("Add New Beer") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            if (showError) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            BeerEditableField(value = name, onValueChange = { name = it }, label = "Name")
            BeerEditableField(value = brewery, onValueChange = { brewery = it }, label = "Brewery")
            BeerEditableField(value = style, onValueChange = { style = it }, label = "Style")
            BeerEditableField(value = abvStr, onValueChange = { abvStr = it }, label = "ABV", keyboardType = KeyboardType.Number)
            BeerEditableField(value = volumeStr, onValueChange = { volumeStr = it }, label = "Volume", keyboardType = KeyboardType.Number)
            BeerEditableField(value = amountStr, onValueChange = { amountStr = it }, label = "Amount", keyboardType = KeyboardType.Number)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onNavigateBack) {
                    Text("Back")
                }

                Button(onClick = {
                    if (validateInputs()) {
                        val newBeer = Beer(
                            id = 0,
                            user = "user",
                            name = name,
                            brewery = brewery,
                            style = style,
                            abv = abvStr.toDouble(),
                            volume = volumeStr.toDouble(),
                            pictureUrl = null,
                            howMany = amountStr.toInt()
                        )
                        onAddBeer(newBeer)
                    } else {
                        showError = true
                    }
                }) {
                    Text("Add Beer")
                }
            }
        }
    }
}


@Composable
fun BeerEditableField(
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    )
}

@Preview
@Composable
fun BeerAddPreview() {
    BeerAddScreen(
        onAddBeer = {},
        onNavigateBack = {}
    )
}
