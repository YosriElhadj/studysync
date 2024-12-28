package com.example.studysync.ui.auth.login

import com.example.studysync.domain.model.User

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val user: User? = null
)