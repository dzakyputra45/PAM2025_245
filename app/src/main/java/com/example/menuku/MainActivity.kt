package com.example.menuku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.menuku.ui.theme.MenuKuTheme
import com.example.menuku.view.LoginScreen
import com.example.menuku.view.controllNavigasi.PetaNavigasi
import com.example.menuku.view.viewmodel.LoginViewModel
import com.example.menuku.view.viewmodel.LoginState



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MenuKuTheme {

                val loginViewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                val loginState by loginViewModel.loginState.collectAsState()

                when (loginState) {

                    is LoginState.Success -> {
                        // 🔥 SUDAH LOGIN → DASHBOARD
                        PetaNavigasi()
                    }

                    else -> {
                        // 🔐 BELUM LOGIN → LOGIN SCREEN
                        LoginScreen(
                            isLoading = loginState is LoginState.Loading,
                            errorMessage = (loginState as? LoginState.Error)?.message,
                            onLoginClick = { email, password ->
                                loginViewModel.login(email, password)
                            }
                        )
                    }
                }
            }
        }
    }
}
