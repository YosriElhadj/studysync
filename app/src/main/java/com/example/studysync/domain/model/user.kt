package com.example.studysync.domain.model


data class User(
    val id: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String? = null,
    val isEmailVerified: Boolean = false
)