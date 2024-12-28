package com.example.studysync.domain.usecase

import com.example.studysync.domain.model.User
import com.example.studysync.domain.reposiroty.AuthRepository
import com.example.studysync.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GoogleSignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Flow<Resource<User>> {
        if (idToken.isBlank()) {
            return kotlinx.coroutines.flow.flow {
                emit(Resource.Error("Invalid Google Sign In token"))
            }
        }

        return repository.googleSignIn(idToken)
    }
}