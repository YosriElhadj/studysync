package com.example.studysync.util

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(
        val message: String,
        val code: Int = -1  // Added error code with default value
    ) : Resource<T>()
    class Loading<T> : Resource<T>()
}