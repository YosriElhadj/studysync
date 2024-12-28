package com.example.studysync.ui.auth.forgot_password

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
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordState())
    val state: StateFlow<ForgotPasswordState> = _state

    fun onEmailChange(email: String) {
        _state.update { it.copy(
            email = email,
            // Reset error when user starts typing
            error = null
        ) }
    }

    fun onResetPasswordClick() {
        val email = state.value.email

        // Basic email validation
        if (email.isBlank()) {
            _state.update { it.copy(error = "Email cannot be empty") }
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _state.update { it.copy(error = "Please enter a valid email address") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val result = authRepository.resetPassword(email)
                result.fold(
                    onSuccess = {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isSuccess = true,
                                successMessage = "Password reset instructions have been sent to your email"
                            )
                        }
                    },
                    onFailure = { exception ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = exception.message ?: "Failed to send reset password email"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    fun clearSuccess() {
        _state.update { it.copy(
            isSuccess = false,
            successMessage = null
        ) }
    }
}

