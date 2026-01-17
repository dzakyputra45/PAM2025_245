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

                val viewModel: LoginViewModel = viewModel()
                val loginState by viewModel.loginState.collectAsState()

                when (val state = loginState) {

                    is LoginState.Success -> {
                        PetaNavigasi()
                    }

                    is LoginState.Error -> {
                        LoginScreen(
                            isLoading = false,
                            errorMessage = state.message,
                            onLoginClick = { email, password ->
                                viewModel.login(email, password)
                            }
                        )
                    }

                    is LoginState.Loading -> {
                        LoginScreen(
                            isLoading = true,
                            errorMessage = null,
                            onLoginClick = { email, password ->
                                viewModel.login(email, password)
                            }
                        )
                    }

                    else -> {
                        LoginScreen(
                            isLoading = false,
                            errorMessage = null,
                            onLoginClick = { email, password ->
                                viewModel.login(email, password)
                            }
                        )
                    }
                }

            }
        }
    }
}