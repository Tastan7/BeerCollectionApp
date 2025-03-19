package com.example.beercollection.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Authentication(
    user: FirebaseUser? = null, // Current user
    message: String = "", // Use message from ViewModel
    signIn: (String, String) -> Unit = { _, _ -> }, // Sign-in function
    register: (String, String) -> Unit = { _, _ -> }, // Register function
    navigateToNextScreen: () -> Unit = {} // Callback for navigation
) {
    // Navigate to next screen if the user is logged in
    if (user != null) {
        LaunchedEffect(user) { // Ensures that navigation happens once
            navigateToNextScreen()
        }
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var emailIsError by remember { mutableStateOf(false) }
    var passwordIsError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") } // Local state for error messages

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Authentication") }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)
        ) {
            // Email Input Field
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailIsError = false // Reset error state on value change
                    errorMessage = "" // Clear any previous error messages
                },
                label = { Text("Email") },
                isError = emailIsError,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (emailIsError) {
                Text("Invalid email", color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Password Input Field
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordIsError = false // Reset error state on value change
                    errorMessage = "" // Clear any previous error messages
                },
                label = { Text("Password") },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (showPassword) "Hide password" else "Show password"
                        )
                    }
                },
                isError = passwordIsError,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (passwordIsError) {
                Text("Invalid password", color = MaterialTheme.colorScheme.error)
            }

            // Show error message if provided
            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error) // Show general error message
            }

            // Show message from ViewModel if provided
            if (message.isNotEmpty()) {
                Text(message, color = MaterialTheme.colorScheme.primary) // Show message
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    emailIsError = email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    passwordIsError = password.isEmpty()

                    if (emailIsError || passwordIsError) {
                        errorMessage = "Please enter valid email and password." // Set error message if validation fails
                    } else {
                        // Call register function
                        register(email.trim(), password.trim()) // Call register function
                    }
                }) {
                    Text("Register")
                }
                Button(onClick = {
                    emailIsError = email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    passwordIsError = password.isEmpty()

                    if (emailIsError || passwordIsError) {
                        errorMessage = "Please enter valid email and password." // Set error message if validation fails
                    } else {
                        signIn(email.trim(), password.trim()) // Call sign-in function
                    }
                }) {
                    Text("Sign In")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthPreview() {
    Authentication()
}

