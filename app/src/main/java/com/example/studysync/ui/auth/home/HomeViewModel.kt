package com.example.studysync.ui.auth.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studysync.domain.reposiroty.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.ui.graphics.Color
import com.example.studysync.domain.model.User
import com.example.studysync.domain.reposiroty.CourseRepository
import com.example.studysync.domain.reposiroty.UserRepository
import com.example.studysync.util.Resource

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository, // Add this repository
    private val courseRepository: CourseRepository // Add this repository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                // Load user profile
                loadUserProfile()
                // Load courses and tasks
                loadUserCourses()
                // Load study statistics
                loadStudyStats()

                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message ?: "Error loading data",
                        isLoading = false
                    )
                }
            }
        }
    }

    private suspend fun loadUserProfile() {
        userRepository.getUserProfile().collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            user = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            error = result.message,
                            isLoading = false
                        )
                    }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private suspend fun loadUserCourses() {
        courseRepository.getUserCourses().collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.update { currentState ->
                        currentState.copy(
                            courses = result.data ?: emptyList(),
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(error = result.message)
                    }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private suspend fun loadStudyStats() {
        userRepository.getStudyStats().collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.update { currentState ->
                        currentState.copy(
                            studyStats = result.data ?: StudyStats(),
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(error = result.message)
                    }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun updateUserProfile(updatedUser: User) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            userRepository.updateProfile(updatedUser).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                user = result.data,
                                isLoading = false,
                                isEditingProfile = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                error = result.message,
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    fun onTaskCheckedChange(taskId: String, isCompleted: Boolean) {
        viewModelScope.launch {
            try {
                courseRepository.updateTaskStatus(taskId, isCompleted).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.update { state ->
                                val updatedTasks = state.upcomingTasks.map { task ->
                                    if (task.id == taskId) task.copy(isCompleted = isCompleted)
                                    else task
                                }
                                state.copy(
                                    upcomingTasks = updatedTasks,
                                    error = null
                                )
                            }
                            // Refresh study stats after task update
                            loadStudyStats()
                        }
                        is Resource.Error -> {
                            _state.update {
                                it.copy(error = result.message)
                            }
                        }
                        is Resource.Loading -> {
                            _state.update { it.copy(isLoading = true) }
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Error updating task")
                }
            }
        }
    }

    fun toggleEditProfile() {
        _state.update { it.copy(isEditingProfile = !it.isEditingProfile) }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    fun refresh() {
        loadInitialData()
    }

    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
                _state.update { HomeState() } // Reset state
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Error during logout")
                }
            }
        }
    }
}