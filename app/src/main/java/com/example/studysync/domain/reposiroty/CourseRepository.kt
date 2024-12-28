package com.example.studysync.domain.reposiroty


import com.example.studysync.ui.auth.home.Course
import com.example.studysync.util.Resource
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    suspend fun getUserCourses(): Flow<Resource<List<Course>>>
    suspend fun updateTaskStatus(taskId: String, isCompleted: Boolean): Flow<Resource<Unit>>
}