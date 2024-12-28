package com.example.studysync.ui.auth.forgot_password

data class ForgotPasswordState(
    val email: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)