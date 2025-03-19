package com.example.beercollection.model

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.beercollection.repository.AuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthenticationViewModel(private val repository: AuthenticationRepository) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    var user: FirebaseUser? by mutableStateOf(auth.currentUser)
        private set

    private val _message = mutableStateOf("")
    val message: State<String> get() = _message

    var isLoading by mutableStateOf(false)

    fun signIn(email: String, password: String) {
        isLoading = true
        repository.signIn(email, password) { isSuccess, error ->
            isLoading = false
            if (isSuccess) {
                user = auth.currentUser
                _message.value = ""
            } else {
                user = null
                _message.value = error ?: "Unknown error"
            }
        }
    }

    fun register(email: String, password: String) {
        isLoading = true
        repository.register(email, password) { isSuccess, error ->
            isLoading = false
            if (isSuccess) {
                _message.value = "Account successfully registered!"
                user = null // Keep the user null on registration
            } else {
                user = null
                _message.value = error ?: "Unknown error"
            }
        }
    }

    fun signOut() {
        repository.signOut()
        user = null
    }
}


// just a test :)