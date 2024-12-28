package com.example.studysync.data.repository

import com.example.studysync.data.local.PreferencesManager
import com.example.studysync.data.remote.api.ApiService
import com.example.studysync.domain.reposiroty.CourseRepository
import com.example.studysync.ui.auth.home.Course
import com.example.studysync.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CourseRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val preferencesManager: PreferencesManager
) : CourseRepository {

    override suspend fun getUserCourses(): Flow<Resource<List<Course>>> = flow {
        emit(Resource.Loading())
        try {
            val token = preferencesManager.getAuthToken() ?: run {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }

            // Assuming you have an endpoint for user courses
            val response = api.getUserCourses("Bearer $token")
            if (response.isSuccessful) {
                val coursesResponse = response.body()
                if (coursesResponse?.data != null) {
                    val courses = coursesResponse.data.map { courseDto ->
                        Course(
                            id = courseDto.id,
                            title = courseDto.title,
                            progress = courseDto.progress,
                            color = courseDto.color
                        )
                    }
                    emit(Resource.Success(courses))
                } else {
                    emit(Resource.Error("No courses found"))
                }
            } else {
                emit(Resource.Error(response.message() ?: "Failed to get courses"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override suspend fun updateTaskStatus(
        taskId: String,
        isCompleted: Boolean
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val token = preferencesManager.getAuthToken() ?: run {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }

            // Assuming you have an endpoint for updating task status
            val response = api.updateTaskStatus(
                "Bearer $token",
                taskId,
                isCompleted
            )
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error(response.message() ?: "Failed to update task"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}