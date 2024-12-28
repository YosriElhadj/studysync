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

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    init {
        loadUserData()
        loadMockData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                authRepository.getCurrentUser()?.let { user ->
                    _state.update { it.copy(userName = user.displayName) }
                } ?: run {
                    _state.update { it.copy(error = "User not found") }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Error loading user data")
                }
            }
        }
    }

    private fun loadMockData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val mockCourses = listOf(
                    Course(
                        id = "1",
                        title = "Mathematics",
                        progress = 0.75f,
                        color = Color(0xFF2196F3)
                    ),
                    Course(
                        id = "2",
                        title = "Physics",
                        progress = 0.45f,
                        color = Color(0xFFF44336)
                    ),
                    Course(
                        id = "3",
                        title = "Computer Science",
                        progress = 0.90f,
                        color = Color(0xFF4CAF50)
                    )
                )

                val mockTasks = listOf(
                    Task(
                        id = "1",
                        title = "Complete Math Assignment",
                        dueDate = "Today",
                        isCompleted = false,
                        courseId = "1",
                        priority = TaskPriority.HIGH
                    ),
                    Task(
                        id = "2",
                        title = "Physics Lab Report",
                        dueDate = "Tomorrow",
                        isCompleted = false,
                        courseId = "2",
                        priority = TaskPriority.MEDIUM
                    ),
                    Task(
                        id = "3",
                        title = "Programming Project",
                        dueDate = "Next Week",
                        isCompleted = true,
                        courseId = "3",
                        priority = TaskPriority.LOW
                    )
                )

                val mockStats = StudyStats(
                    totalStudyTime = 420f, // 7 hours
                    completedTasks = 15,
                    streakDays = 5,
                    weeklyGoal = 2100f, // 35 hours
                    weeklyProgress = 1260f // 21 hours
                )

                _state.update {
                    it.copy(
                        courses = mockCourses,
                        upcomingTasks = mockTasks,
                        studyStats = mockStats,
                        isLoading = false
                    )
                }
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

    fun onTaskCheckedChange(taskId: String, isCompleted: Boolean) {
        _state.update { state ->
            val updatedTasks = state.upcomingTasks.map { task ->
                if (task.id == taskId) task.copy(isCompleted = isCompleted)
                else task
            }
            state.copy(upcomingTasks = updatedTasks)
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    fun refresh() {
        loadUserData()
        loadMockData()
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}