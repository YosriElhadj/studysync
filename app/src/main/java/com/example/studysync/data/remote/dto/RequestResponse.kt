package com.example.studysync.data.remote.dto

import android.graphics.Color

data class LoginRequest(
    val email: String,
    val password: String
)

// data/remote/dto/LoginResponse.kt
data class LoginResponse(
    val message: String,
    val token: String,
    val user: UserDto
)

// data/remote/dto/UserDto.kt
data class UserDto(
    val id: String,
    val email: String,
    val displayName: String,
    // Keep these as nullable since they'll be set later
    val nom: String? = null,
    val prenom: String? = null,
    val role: String? = null,
    val date_de_naissance: String? = null,
    val genre: String? = null,
    val numero_telephone: String? = null,
    val adresse: String? = null,
    val photo: String? = null,
    val institution: String? = null
)
data class StudyStatsDto(
    val totalStudyTime: Float,
    val completedTasks: Int,
    val streakDays: Int,
    val weeklyGoal: Float,
    val weeklyProgress: Float
)

data class CourseDto(
    val id: String,
    val title: String,
    val progress: Float,
    val color: Color
)

// data/remote/dto/ApiResponse.kt
data class ApiResponse<T>(
    val message: String,
    val data: T?
)