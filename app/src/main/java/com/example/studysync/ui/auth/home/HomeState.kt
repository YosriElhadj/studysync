package com.example.studysync.ui.auth.home

import androidx.compose.ui.graphics.Color

data class HomeState(
    val userName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val courses: List<Course> = emptyList(),
    val upcomingTasks: List<Task> = emptyList(),
    val studyStats: StudyStats = StudyStats()
)

data class Course(
    val id: String,
    val title: String,
    val progress: Float, // 0f to 1f
    val color: Color
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