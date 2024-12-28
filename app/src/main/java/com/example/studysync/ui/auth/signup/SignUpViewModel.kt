package com.example.studysync.ui.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studysync.domain.reposiroty.AuthRepository
import com.example.studysync.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state

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
        validatePasswordMatch()
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _state.update {
            it.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = null,
                error = null
            )
        }
        validatePasswordMatch()
    }

    fun onDisplayNameChange(displayName: String) {
        _state.update {
            it.copy(
                displayName = displayName,
                displayNameError = null,
                error = null
            )
        }
    }

    private fun validatePasswordMatch() {
        val state = _state.value
        if (state.password.isNotBlank() && state.confirmPassword.isNotBlank()
            && state.password != state.confirmPassword) {
            _state.update {
                it.copy(confirmPasswordError = "Passwords do not match")
            }
        }
    }

    private fun validateInput(): Boolean {
        val state = _state.value
        var isValid = true

        if (state.displayName.isBlank()) {
            _state.update {
                it.copy(displayNameError = "Display name cannot be empty")
            }
            isValid = false
        }

        if (state.email.isBlank()) {
            _state.update {
                it.copy(emailError = "Email cannot be empty")
            }
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _state.update {
                it.copy(emailError = "Invalid email format")
            }
            isValid = false
        }

        if (state.password.isBlank()) {
            _state.update {
                it.copy(passwordError = "Password cannot be empty")
            }
            isValid = false
        } else if (state.password.length < 6) {
            _state.update {
                it.copy(passwordError = "Password must be at least 6 characters")
            }
            isValid = false
        }

        if (state.confirmPassword != state.password) {
            _state.update {
                it.copy(confirmPasswordError = "Passwords do not match")
            }
            isValid = false
        }

        return isValid
    }

    fun onSignUpClick() {
        if (!validateInput()) {
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            authRepository.signUp(
                email = _state.value.email,
                password = _state.value.password,
                displayName = _state.value.displayName
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isSuccess = true,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
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

    fun clearFieldErrors() {
        _state.update {
            it.copy(
                emailError = null,
                passwordError = null,
                confirmPasswordError = null,
                displayNameError = null,
                error = null
            )
        }
    }
}