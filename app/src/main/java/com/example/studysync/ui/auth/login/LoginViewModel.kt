package com.example.studysync.ui.auth.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studysync.domain.usecase.GoogleSignInUseCase
import com.example.studysync.domain.usecase.LoginUseCase
import com.example.studysync.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun onEmailChange(email: String) {
        _state.update {
            it.copy(
                email = email,
                emailError = null,
                error = null
            )
        }
    }

    fun onPasswordChange(password: String) {
        _state.update {
            it.copy(
                password = password,
                passwordError = null,
                error = null
            )
        }
    }

    private fun validateInputs(): Boolean {
        val emailResult = validateEmail()
        val passwordResult = validatePassword()
        return emailResult && passwordResult
    }

    private fun validateEmail(): Boolean {
        val email = state.value.email
        return when {
            email.isBlank() -> {
                _state.update { it.copy(emailError = "Email cannot be empty") }
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _state.update { it.copy(emailError = "Please enter a valid email address") }
                false
            }
            else -> true
        }
    }

    private fun validatePassword(): Boolean {
        val password = state.value.password
        return when {
            password.isBlank() -> {
                _state.update { it.copy(passwordError = "Password cannot be empty") }
                false
            }
            password.length < 6 -> {
                _state.update { it.copy(passwordError = "Password must be at least 6 characters") }
                false
            }
            else -> true
        }
    }

    fun login() {
        if (!validateInputs()) {
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            loginUseCase(state.value.email, state.value.password).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = null,
                                isSuccess = true,
                                user = result.data
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message,
                                isSuccess = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun googleSignIn() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            googleSignInUseCase("").collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = null,
                                isSuccess = true,
                                user = result.data
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message,
                                isSuccess = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}