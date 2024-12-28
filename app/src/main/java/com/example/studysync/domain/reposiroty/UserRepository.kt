package com.example.studysync.domain.reposiroty

import com.example.studysync.domain.model.User
import com.example.studysync.ui.auth.home.StudyStats
import com.example.studysync.util.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserProfile(): Flow<Resource<User>>
    suspend fun updateProfile(user: User): Flow<Resource<User>>
    suspend fun getStudyStats(): Flow<Resource<StudyStats>>
}