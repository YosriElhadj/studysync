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
        _state.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _state.update { it.copy(confirmPassword = confirmPassword) }
    }

    fun onDisplayNameChange(displayName: String) {
        _state.update { it.copy(displayName = displayName) }
    }

    private fun validateInput(): String? {
        val state = _state.value
        return when {
            state.email.isBlank() -> "Email cannot be empty"
            state.password.isBlank() -> "Password cannot be empty"
            state.confirmPassword.isBlank() -> "Confirm password cannot be empty"
            state.displayName.isBlank() -> "Display name cannot be empty"
            state.password != state.confirmPassword -> "Passwords do not match"
            state.password.length < 6 -> "Password must be at least 6 characters"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches() -> "Invalid email format"
            else -> null
        }
    }

    fun onSignUpClick() {
        val validationError = validateInput()
        if (validationError != null) {
            _state.update { it.copy(error = validationError) }
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
}