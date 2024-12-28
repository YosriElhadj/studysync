package com.example.studysync.ui.auth.home

import com.example.studysync.domain.model.User



data class Course(
    val id: String,
    val title: String,
    val progress: Float, // 0f to 1f
    val color: android.graphics.Color
)

data class Task(
    val id: String,
    val title: String,
    val dueDate: String,
    val isCompleted: Boolean,
    val courseId: String,
    val priority: TaskPriority
)

enum class TaskPriority {
    HIGH, MEDIUM, LOW
}

data class StudyStats(
    val totalStudyTime: Float = 0f, // in minutes
    val completedTasks: Int = 0,
    val streakDays: Int = 0,
    val weeklyGoal: Float = 0f,
    val weeklyProgress: Float = 0f
)

data class HomeState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val studyStats: StudyStats = StudyStats(),
    val courses: List<Course> = emptyList(),
    val upcomingTasks: List<Task> = emptyList(),
    val isEditingProfile: Boolean = false
)
