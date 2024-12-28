package com.example.studysync.ui.auth.login

import com.example.studysync.domain.model.User

data class LoginState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val user: User? = null
)