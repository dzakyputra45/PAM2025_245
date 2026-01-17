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

        // 🔐 VALIDASI INPUT
        if (email.isBlank()) {
            _loginState.value = LoginState.Error("Email wajib diisi")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _loginState.value = LoginState.Error("Format email tidak valid")
            return
        }

        if (password.length < 6) {
            _loginState.value = LoginState.Error("Password minimal 6 karakter")
            return
        }

        _loginState.value = LoginState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user

                // 🔒 OPSIONAL (RECOMMENDED)
                if (user != null && !user.isEmailVerified) {
                    auth.signOut()
                    _loginState.value = LoginState.Error("Email belum diverifikasi")
                    return@addOnSuccessListener
                }

                _loginState.value = LoginState.Success
            }
            .addOnFailureListener { e ->
                _loginState.value = LoginState.Error(
                    e.message ?: "Login gagal"
                )
            }
    }
}
