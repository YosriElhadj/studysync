package com.example.studysync.data.remote.api

import com.example.studysync.data.remote.dto.*
import retrofit2.Response  // Change this import
import retrofit2.http.*

interface ApiService {
    @POST("api/users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/users/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<ApiResponse<UserDto>>

    @GET("api/users/profile-details")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): Response<ApiResponse<UserDto>>

    @PUT("api/users/profile-update")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Body updateRequest: UpdateProfileRequest
    ): Response<ApiResponse<UserDto>>

    @POST("api/users/reset-password")
    suspend fun resetPassword(@Body email: String): Response<ApiResponse<Nothing>>

    @POST("api/users/auth/google")
    suspend fun googleSignIn(@Body idToken: String): Response<LoginResponse>

    @GET("api/users/study-stats")
    suspend fun getStudyStats(
        @Header("Authorization") token: String
    ): Response<ApiResponse<StudyStatsDto>>

    @GET("api/courses")
    suspend fun getUserCourses(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<CourseDto>>>

    @POST("api/tasks/{taskId}/status")
    suspend fun updateTaskStatus(
        @Header("Authorization") token: String,
        @Path("taskId") taskId: String,
        @Body isCompleted: Boolean
    ): Response<ApiResponse<Unit>>
}