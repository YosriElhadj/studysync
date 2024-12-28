// data/remote/dto/RegisterRequest.kt
package com.example.studysync.data.remote.dto

data class RegisterRequest(
    val email: String,
    val password: String,
    val displayName: String
)