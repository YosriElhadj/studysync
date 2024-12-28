// data/remote/dto/UpdateProfileRequest.kt
package com.example.studysync.data.remote.dto

data class UpdateProfileRequest(
    val nom: String?,
    val prenom: String?,
    val email: String?,
    val date_de_naissance: String?,
    val genre: String?,
    val numero_telephone: String?,
    val adresse: String?,
    val photo: String?,
    val institution: String?
)