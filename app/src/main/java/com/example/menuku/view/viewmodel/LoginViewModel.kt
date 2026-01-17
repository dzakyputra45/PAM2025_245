package com.example.menuku.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {

        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Email dan password wajib diisi")
            return
        }

        _loginState.value = LoginState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // 🔥 LANGSUNG SUKSES
                _loginState.value = LoginState.Success
            }
            .addOnFailureListener { e ->
                _loginState.value =
                    LoginState.Error(e.message ?: "Login gagal")
            }
    }
}
