package com.example.studysync.domain.reposiroty


import com.example.studysync.domain.model.User
import com.example.studysync.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Flow<Resource<User>>
    suspend fun googleSignIn(idToken: String): Flow<Resource<User>>
    suspend fun logout()
    fun getCurrentUser(): User?
}