package com.example.studysync.data.repository

import com.example.studysync.data.local.PreferencesManager
import com.example.studysync.data.remote.api.ApiService
import com.example.studysync.data.remote.dto.UpdateProfileRequest
import com.example.studysync.domain.model.User
import com.example.studysync.domain.reposiroty.UserRepository
import com.example.studysync.ui.auth.home.StudyStats
import com.example.studysync.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val preferencesManager: PreferencesManager
) : UserRepository {

    override suspend fun getUserProfile(): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val token = preferencesManager.getAuthToken() ?: run {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }

            val response = api.getUserProfile("Bearer $token")
            if (response.isSuccessful) {
                val userResponse = response.body()
                if (userResponse?.data != null) {
                    val user = User(
                        id = userResponse.data.id,
                        email = userResponse.data.email,
                        displayName = userResponse.data.displayName,
                        nom = userResponse.data.nom,
                        prenom = userResponse.data.prenom,
                        dateDeNaissance = userResponse.data.date_de_naissance,
                        genre = userResponse.data.genre,
                        numeroTelephone = userResponse.data.numero_telephone,
                        adresse = userResponse.data.adresse,
                        photo = userResponse.data.photo,
                        institution = userResponse.data.institution
                    )
                    emit(Resource.Success(user))
                } else {
                    emit(Resource.Error("User data not found"))
                }
            } else {
                emit(Resource.Error(response.message() ?: "Failed to get user profile"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override suspend fun updateProfile(user: User): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val token = preferencesManager.getAuthToken() ?: run {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }

            val updateRequest = UpdateProfileRequest(
                nom = user.nom,
                prenom = user.prenom,
                email = user.email,
                date_de_naissance = user.dateDeNaissance,
                genre = user.genre,
                numero_telephone = user.numeroTelephone,
                adresse = user.adresse,
                photo = user.photo,
                institution = user.institution
            )

            val response = api.updateUserProfile("Bearer $token", updateRequest)
            if (response.isSuccessful) {
                val userResponse = response.body()
                if (userResponse?.data != null) {
                    emit(Resource.Success(user))
                } else {
                    emit(Resource.Error("Failed to update profile"))
                }
            } else {
                emit(Resource.Error(response.message() ?: "Failed to update profile"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override suspend fun getStudyStats(): Flow<Resource<StudyStats>> = flow {
        emit(Resource.Loading())
        try {
            val token = preferencesManager.getAuthToken() ?: run {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }

            // Assuming you have an endpoint for study stats
            val response = api.getStudyStats("Bearer $token")
            if (response.isSuccessful) {
                val statsResponse = response.body()
                if (statsResponse?.data != null) {
                    val stats = StudyStats(
                        totalStudyTime = statsResponse.data.totalStudyTime,
                        completedTasks = statsResponse.data.completedTasks,
                        streakDays = statsResponse.data.streakDays,
                        weeklyGoal = statsResponse.data.weeklyGoal,
                        weeklyProgress = statsResponse.data.weeklyProgress
                    )
                    emit(Resource.Success(stats))
                } else {
                    emit(Resource.Error("Study stats not found"))
                }
            } else {
                emit(Resource.Error(response.message() ?: "Failed to get study stats"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}