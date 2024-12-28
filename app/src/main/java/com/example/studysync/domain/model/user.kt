package com.example.studysync.domain.model



data class User(
    val id: String = "",
    val email: String = "",
    val displayName: String = "",
    val nom: String? = null,
    val prenom: String? = null,
    val photoUrl: String? = null,
    val isEmailVerified: Boolean = false,
    val dateDeNaissance: String? = null,
    val genre: String? = null,
    val numeroTelephone: String? = null,
    val role: String = "user",
    val adresse: String? = null,
    val institution: String? = null,
    val photo:String?=null,
    // Study-related fields
    val interests: List<String> = emptyList(),
    val createdAt: String? = null,
    val updatedAt: String? = null
)