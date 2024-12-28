// AuthRepositoryImpl.kt
package com.example.studysync.data.repository

import com.example.studysync.data.local.PreferencesManager
import com.example.studysync.data.mapper.toUser
import com.example.studysync.data.remote.api.ApiService
import com.example.studysync.data.remote.dto.LoginRequest
import com.example.studysync.data.remote.dto.RegisterRequest
import com.example.studysync.domain.model.User
import com.example.studysync.domain.reposiroty.AuthRepository
import com.example.studysync.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val preferencesManager: PreferencesManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                preferencesManager.saveAuthToken(loginResponse.token)

                val user = User(
                    id = loginResponse.user.id,
                    email = loginResponse.user.email,
                    displayName = "${loginResponse.user.prenom} ${loginResponse.user.nom}"
                )
                emit(Resource.Success(user))
            } else {
                val errorBody = response.errorBody()?.string()
                emit(Resource.Error(
                    message = errorBody ?: "Login failed",
                    code = response.code()
                ))
            }
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "An unexpected error occurred",
                code = -1
            ))
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        displayName: String
    ): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())

            val request = RegisterRequest(
                email = email,
                password = password,
                displayName = displayName
            )

            val response = apiService.register(request)

            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.data != null) {
                    emit(Resource.Success(apiResponse.data.toUser()))
                } else {
                    emit(Resource.Error(apiResponse?.message ?: "Registration failed"))
                }
            } else {
                emit(Resource.Error(response.message() ?: "Registration failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override suspend fun googleSignIn(idToken: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.googleSignIn(idToken)
            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                preferencesManager.saveAuthToken(loginResponse.token)

                val user = User(
                    id = loginResponse.user.id,
                    email = loginResponse.user.email,
                    displayName = "${loginResponse.user.prenom} ${loginResponse.user.nom}"
                )
                emit(Resource.Success(user))
            } else {
                val errorBody = response.errorBody()?.string()
                emit(Resource.Error(
                    message = errorBody ?: "Google Sign In failed",
                    code = response.code()
                ))
            }
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "An unexpected error occurred",
                code = -1
            ))
        }
    }

    override suspend fun logout() {
        preferencesManager.clearAuthData()
    }

    override fun getCurrentUser(): User? {
        // TODO: Implement this to return the cached user data
        return null
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            val response = apiService.resetPassword(email)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Password reset failed"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}