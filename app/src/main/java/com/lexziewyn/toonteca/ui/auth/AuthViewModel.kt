package com.lexziewyn.toonteca.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lexziewyn.toonteca.data.remote.RetrofitInstance
import com.lexziewyn.toonteca.data.remote.dto.LoginRequest
import com.lexziewyn.toonteca.data.remote.dto.RegisterRequest
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    object Success : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class AuthViewModel : ViewModel() {

    var uiState by mutableStateOf<AuthUiState>(AuthUiState.Idle)
        private set

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            uiState = AuthUiState.Error("Ingresa correo y contraseña")
            return
        }

        viewModelScope.launch {
            uiState = AuthUiState.Loading
            try {
                val response = RetrofitInstance.api.login(LoginRequest(email, pass))
                RetrofitInstance.token = response.accessToken
                uiState = AuthUiState.Success
            } catch (e: Exception) {
                uiState = AuthUiState.Error("Credenciales inválidas o error de conexión")
            }
        }
    }

    fun register(name: String, email: String, pass: String) {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            uiState = AuthUiState.Error("Todos los campos son obligatorios")
            return
        }

        viewModelScope.launch {
            uiState = AuthUiState.Loading
            try {
                val response = RetrofitInstance.api.register(RegisterRequest(name, email, pass))
                RetrofitInstance.token = response.accessToken
                uiState = AuthUiState.Success
            } catch (e: Exception) {
                uiState = AuthUiState.Error("Error al registrar usuario")
            }
        }
    }

    fun resetState() {
        uiState = AuthUiState.Idle
    }
}