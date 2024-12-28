package com.example.studysync.domain.usecase

import com.example.studysync.domain.model.User
import com.example.studysync.domain.reposiroty.AuthRepository
import com.example.studysync.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Flow<Resource<User>> {
        if (email.isBlank()) {
            return kotlinx.coroutines.flow.flow {
                emit(Resource.Error("Email cannot be empty"))
            }
        }
        if (password.isBlank()) {
            return kotlinx.coroutines.flow.flow {
                emit(Resource.Error("Password cannot be empty"))
            }
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return kotlinx.coroutines.flow.flow {
                emit(Resource.Error("Invalid email format"))
            }
        }
        if (password.length < 6) {
            return kotlinx.coroutines.flow.flow {
                emit(Resource.Error("Password must be at least 6 characters"))
            }
        }

        return repository.login(email, password)
    }
}